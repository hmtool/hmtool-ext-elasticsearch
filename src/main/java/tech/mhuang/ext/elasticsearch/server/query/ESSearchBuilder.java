package tech.mhuang.ext.elasticsearch.server.query;

import tech.mhuang.ext.elasticsearch.model.query.ESOrder;
/**
 *
 * ES查询封装
 *
 * @author zhangxh
 * @since 1.0.0
 */
public class ESSearchBuilder {


    private QueryContext queryContext = new QueryContext();

    /**
     * 新增查询条件
     * @param field
     * @param type
     * @param values
     * @return
     */
    public ESSearchBuilder addCondition(String field, OperatorType type, Object ...values){
        this.addContext(field,type, QueryQuota.CONDITION,values);
        return this;
    }


    /**
     * 新增类似于 and 操作符
     * @return
     */
   public ESSearchBuilder and(){
       this.addContext(null,null, QueryQuota.AND,null);
       return this;
   }

    /**
     * 添加查询上下文
     * @param field
     * @param type
     * @param queryQuota
     * @param values
     */
    private void addContext(String field, OperatorType type, QueryQuota queryQuota, Object ...values) {
        OperatorContext context = new OperatorContext();
        context.setFieldName(field);
        context.setQueryQuota(queryQuota);
        context.setValues(values);
        context.setOperateType(type);
        queryContext.addContext(context);
    }

    /**
     * 新增 操作符 or
     * @return
     */
    public ESSearchBuilder or(){
        this.addContext(null,null,QueryQuota.OR,null);
        return this;
    }

    /**
     * 新增左括号
     * @return
     */
    public ESSearchBuilder startInnerCondition(){
        this.addContext(null,null,QueryQuota.LT,null);
        return this;
    }

    /**
     * 新增右边括号
     * @return
     */
    public ESSearchBuilder endInnerCondition(){
        this.addContext(null,null,QueryQuota.GT,null);
        return this;
    }

    /**
     * 分页查询的srollerId
     * @param scrollId 分页的scrollId
     * @return
     */
    public ESSearchBuilder scrollId(String scrollId){
        this.queryContext.scrollId(scrollId);
        return this;
    }

    /**
     * 集合查询的开始值
     * @param from
     * @return
     */
    public ESSearchBuilder from(Integer from) {
        this.queryContext.from(from);
        return this;
    }

    /**
     * 分页Scroll查询的超时时间
     * @param minute
     * @return
     */
    public ESSearchBuilder scrollTimeout(long minute) {
        this.queryContext.scrollTimeout(minute);
        return this;
    }

    /**
     * 返回条数
     * @param size
     * @return
     */
    public ESSearchBuilder size(Integer size) {
         this.queryContext.size(size);
         return this;
    }

    /**
     * 搜索的索引
     * @param indexNames
     * @return
     */
    public ESSearchBuilder indexNames(String[] indexNames) {
        this.queryContext.indexNames(indexNames);
        return this;
    }

    /**
     * 查询排除字段
     * @param excludeFields
     * @return
     */
    public ESSearchBuilder excludeFields(String[] excludeFields) {
        this.queryContext.excludeFields(excludeFields);
        return this;
    }

    /**
     * 查询包含字段
     * @param includeFields
     * @return
     */
    public ESSearchBuilder includeFields(String[] includeFields) {
        this.queryContext.includeFields(includeFields);
        return this;
    }

    /**
     * 排序
     * @param field
     * @param orderType
     * @return
     */
    public ESSearchBuilder order(String field, OrderType orderType) {
        this.queryContext.addOrder(new ESOrder(field,orderType));
        return this;
    }

    /**
     * 不带条件查询
     * @return
     */
    public ESSearchBuilder all(){
        return this;
    }

    /**
     * 查询全文
     * @return
     */
    public QueryContext getQueryContext() {
        return queryContext;
    }
}
