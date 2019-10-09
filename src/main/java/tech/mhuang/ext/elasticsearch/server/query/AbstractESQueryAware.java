package tech.mhuang.ext.elasticsearch.server.query;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import tech.mhuang.core.util.CollectionUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
/**
 *
 * 查询封装类
 *
 * @author zhangxh
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractESQueryAware implements ESQueryAware {

    /**
     * 解析非page
     * @param searchRequest
     * @param sourceBuilder
     * @param queryContext
     */
    protected  void analizeNotScollContext(SearchRequest searchRequest, SearchSourceBuilder sourceBuilder, QueryContext queryContext){
        BoolQueryBuilder boolQueryBuilder =   this.analizeCommonContext(sourceBuilder,queryContext);
        if(queryContext.getFrom()!=null){
            log.debug("开始  {}",queryContext.getFrom());
            sourceBuilder.from(queryContext.getFrom());
        }
        if(queryContext.getIndexNames()!=null && queryContext.getIndexNames().length>0){
            log.debug("设置搜索的索引  {}",queryContext.getIndexNames());
            searchRequest.indices(queryContext.getIndexNames());
        }
        sourceBuilder.query(boolQueryBuilder);
    }

    private BoolQueryBuilder analizeCommonContext(SearchSourceBuilder sourceBuilder, QueryContext queryContext) {
        BoolQueryBuilder boolQueryBuilder = this.analizeOnlyContexts(queryContext);
        if(queryContext.getSize()!=null){
            log.debug("限制条数 {}",queryContext.getSize());
            sourceBuilder.size(queryContext.getSize());
        }
        if(queryContext.getOrders()!=null && queryContext.getOrders().size()>0){
            //不支持并发
            queryContext.getOrders().forEach(t->{
                sourceBuilder.sort(new FieldSortBuilder(t.getFieldName()).
                        order(t.getOrderType()==OrderType.ASC?SortOrder.ASC:
                                SortOrder.DESC));
            });
        }
        return boolQueryBuilder;
    }


    /**
     * 解析page
     * @param searchRequest
     * @param queryContext
     */
    protected  void analizeScollContext(SearchScrollRequest searchRequest,  QueryContext queryContext){
            searchRequest.scroll(TimeValue.timeValueMinutes(queryContext.getScrollTimeout()==null?QueryContext.SCROLL_TIMEOUT_DEFAULT:queryContext.getScrollTimeout()));
    }

    /**
     * 分页第一次查询
     * @param searchRequest
     * @param queryContext
     */
    protected  void analizeFirstScollContext(SearchRequest searchRequest, QueryContext queryContext){
        searchRequest.scroll(TimeValue.timeValueMinutes(queryContext.getScrollTimeout()==null?QueryContext.SCROLL_TIMEOUT_DEFAULT:queryContext.getScrollTimeout()));
    }


    protected  BoolQueryBuilder analizeOnlyContexts(QueryContext queryContext){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<OperatorContext> temps = new ArrayList<>(
                queryContext.getContexts());
        Stack<String> params = new Stack<>();
        if(CollectionUtil.isEmpty(temps)){
            log.debug("没有任何条件");
            QueryBuilder queryBuilder= QueryBuilders.matchAllQuery();
            boolQueryBuilder.must(queryBuilder);
        }else {
            if(temps.size()<=1){
                OperatorContext context = temps.get(0);
                if(context.getQueryQuota() == QueryQuota.CONDITION){
                    params.push(this.getCalcMessage(context));
                    boolQueryBuilder.must(this.addCondition(context));
                }else{
                    log.debug("没有任何条件");
                    QueryBuilder queryBuilder= QueryBuilders.matchAllQuery();
                    boolQueryBuilder.must(queryBuilder);
                }
            }else{
                //解析计算后缀表达式
                Stack<OperatorContext> stacks = new Stack<>();
                //后缀表达式
                List<OperatorContext> afterStacks = new ArrayList<>();
                for (OperatorContext t : temps) {
                    switch (t.getQueryQuota()) {
                        case GT:
                            while (!stacks.isEmpty()) {
                                OperatorContext temp = stacks.peek();
                                if (temp.getQueryQuota() == QueryQuota.LT) {
                                    stacks.pop();
                                    break;
                                } else {
                                    temp = stacks.pop();
                                    afterStacks.add(temp);
                                }
                            }
                            break;
                        case LT:
                        case AND:
                        case OR:
                            stacks.push(t);
                            break;
                        case CONDITION://参数信息
                            afterStacks.add(t);
                            break;
                    }
                }
                if(!stacks.isEmpty()){
                    while (!stacks.isEmpty()) {
                        afterStacks.add(stacks.pop());
                    }
                }
                Optional brackets = afterStacks.parallelStream().filter(t->t.getQueryQuota() == QueryQuota.GT ||
                        t.getQueryQuota() == QueryQuota.LT).findFirst();
                if(brackets.isPresent()){
                    throw new IllegalArgumentException("条件括号不匹配！");
                }
                Stack fixStack = new Stack();
                for (OperatorContext t : afterStacks) {
                    if(t.getQueryQuota() == QueryQuota.AND ||
                            t.getQueryQuota() == QueryQuota.OR){
                        //这两个是需要操作的。
                        if(fixStack.size()<2){
                            throw new IllegalArgumentException("and 或者 or的运算参数不匹配");
                        }
                        Object first =  fixStack.pop();
                        OperatorContext second = (OperatorContext) fixStack.pop();
                        QueryBuilder resultBuild = null;
                        QueryBuilder firstBuilder = null;
                        QueryBuilder secondBuilder = this.addCondition(second);
                        if(OperatorContext.class.isInstance(first)){
                            firstBuilder =
                                    this.addCondition((OperatorContext)first);
                        }else{
                            firstBuilder = (QueryBuilder) first;
                        }
                        String calcMessageSecond =this.getCalcMessage(second);
                        String calcFistMessage = null;
                        if(OperatorContext.class.isInstance(first)){
                            params.push(")");
                            params.push(calcMessageSecond);
                            params.push(t.getQueryQuota().getMessage());
                            calcFistMessage =this.getCalcMessage( (OperatorContext) first);
                        }else{
                            params.push(t.getQueryQuota().getMessage());
                            params.push(")");
                            params.push(calcMessageSecond);
                        }
                        if(t.getQueryQuota() == QueryQuota.AND){
                            resultBuild = QueryBuilders.boolQuery().must(firstBuilder).must(secondBuilder);
                        }else{
                            resultBuild = QueryBuilders.boolQuery().should(firstBuilder).should(secondBuilder);
                        }
                        if(calcFistMessage!=null){
                            params.push(calcFistMessage);
                        }
                        params.push("(");
                        fixStack.push(resultBuild);
                    }else{
                        fixStack.push(t);
                    }
                }
                boolQueryBuilder = (BoolQueryBuilder) fixStack.pop();
            }
            this.printMessage(params);
     }
     return boolQueryBuilder;
    }

    protected  void printMessage(Stack<String> params){
        if(log.isDebugEnabled() && !params.isEmpty()){
            StringBuilder builder = new StringBuilder();
            while(!params.isEmpty()){
                builder.append(" ").append(params.pop()).append(" ");
            }
            log.debug("搜索条件："+builder.toString());
        }

    }


    protected QueryBuilder addCondition(OperatorContext context) {
             QueryBuilder  queryBuilder = null;
             switch (context.getOperateType()){
                case EQUALS:
                    checkValue(1,context);
                    queryBuilder = QueryBuilders.termsQuery(context.getFieldName(),context.getValues());
                    break;
                case LIKE:
                    checkValue(1,context);
                    queryBuilder = QueryBuilders.wildcardQuery(context.getFieldName(),"*".concat(context.getValues()[0].toString()).concat("*"));
                    break;
                case GT:
                    checkValue(1,context);
                    queryBuilder = QueryBuilders.rangeQuery(context.getFieldName()).
                            from(context.getValues()[0],false);
                    break;
                case LT:
                    checkValue(1,context);
                    queryBuilder = QueryBuilders.rangeQuery(context.getFieldName()).
                            to(context.getValues()[0],false);
                    break;
                case NOTEQUALS:
                    checkValue(1,context);
                    queryBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery(context.getFieldName(),context.getValues()));
                    break;
                case GTE:
                    checkValue(1,context);
                    queryBuilder = QueryBuilders.rangeQuery(context.getFieldName()).
                            from(context.getValues()[0],true);
                    break;
                case LTE:
                    checkValue(1,context);
                    queryBuilder = QueryBuilders.rangeQuery(context.getFieldName()).
                            to(context.getValues()[0],true);
                    break;
                case BETWEENIN_CLUDE:
                    checkValue(2,context);
                    queryBuilder = QueryBuilders.rangeQuery(context.getFieldName()).
                            from(context.getValues()[0],true).to(context.getValues()[1],
                            true);
                    break;
                case BETWEEN_INCLUDE_LEFT:
                    checkValue(2,context);
                    queryBuilder = QueryBuilders.rangeQuery(context.getFieldName()).
                            from(context.getValues()[0],true).to(context.getValues()[1],
                            false);
                    break;
                case BETWEEN_INCLUDE_RIGHT:
                    checkValue(2,context);
                    queryBuilder = QueryBuilders.rangeQuery(context.getFieldName()).
                            from(context.getValues()[0],false).to(context.getValues()[1],
                            true);
                    break;
                case BETWEEN_NOT_INCLUDE:
                    checkValue(2,context);
                    queryBuilder = QueryBuilders.rangeQuery(context.getFieldName()).
                            from(context.getValues()[0],false).to(context.getValues()[1],
                            false);
                    break;
                case REGEX:
                    checkValue(1,context);
                    queryBuilder = QueryBuilders.regexpQuery(context.getFieldName(),context.getValues()[0].toString());
                    break;
        }
        return queryBuilder;
    }


    protected  void checkValue(int length, OperatorContext context){
         if(context.getValues().length<length){
             throw new IllegalArgumentException(context.getFieldName().concat(" values argument length less than ").concat(length+""));
         }
        for(Object value :  context.getValues()){
            if(value == null ){
                throw new IllegalArgumentException(context.getFieldName().concat(" exists argument value  is null "));
            }
         }
    }


    protected  String getCalcMessage(OperatorContext second){
        MessageFormat format = new MessageFormat(second.getOperateType().getMessage());
        Object[] messages = new Object[second.getValues().length+1];
        messages[0] = second.getFieldName();
        System.arraycopy(second.getValues(),0,messages,1,second.getValues().length);
        return format.format(messages);
    }

 }
