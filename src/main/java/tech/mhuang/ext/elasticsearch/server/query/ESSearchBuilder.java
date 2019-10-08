package tech.mhuang.ext.elasticsearch.server.query;

import tech.mhuang.ext.elasticsearch.model.query.ESOrder;
/**
 * @ClassName EsSearchBuilder
 * @Description: 查询条件封装类
 * @Author admin
 * @Date 2019/05/30 9:32
 * @Version 0.0.3.3.2
 */
public class ESSearchBuilder {


    private QueryContext queryContext = new QueryContext();

    /**
    * @Title addCondition
    * @Author admin
    * @Description 新增查询条件
    * @Date 14:34 2019/05/30
    * @Param [field, type, values]
    * @return com.mhuang.elk.common.source.crud.query.EsSearchBuilder
    */
    public ESSearchBuilder addCondition(String field, OperatorType type, Object ...values){
        this.addContext(field,type, QueryQuota.CONDITION,values);
        return this;
    }


   /**
   * @Title and
   * @Author admin
   * @Description 新增类似于 and 操作符
   * @Date 14:35 2019/05/30
   * @return com.mhuang.elk.common.source.crud.query.EsSearchBuilder
   */
   public ESSearchBuilder and(){
       this.addContext(null,null, QueryQuota.AND,null);
       return this;
   }

   /**
   * @Title addContext
   * @Author admin
   * @Description 添加查询上下文
   * @Date 14:35 2019/05/30
   * @Param [field, type, queryQuota, values]
   * @return void
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
    * @Title or
    * @Author admin
    * @Description 新增 操作符 or
    * @Date 14:35 2019/05/30
    * @return com.mhuang.elk.common.source.crud.query.EsSearchBuilder
    */
    public ESSearchBuilder or(){
        this.addContext(null,null,QueryQuota.OR,null);
        return this;
    }

    /**
    * @Title startInnerCondition
    * @Author admin
    * @Description 新增左括号
    * @Date 14:36 2019/05/30
    * @Param []
    * @return com.mhuang.elk.common.source.crud.query.EsSearchBuilder
    */
    public ESSearchBuilder startInnerCondition(){
        this.addContext(null,null,QueryQuota.LT,null);
        return this;
    }

    /**
    * @Title endInnerCondition
    * @Author admin
    * @Description 新增右边括号
    * @Date 14:36 2019/05/30
    * @return com.mhuang.elk.common.source.crud.query.EsSearchBuilder
    */
    public ESSearchBuilder endInnerCondition(){
        this.addContext(null,null,QueryQuota.GT,null);
        return this;
    }

    /**
    * @Title scrollId
    * @Author admin
    * @Description 分页查询的srollerId
    * @Date 14:37 2019/05/30
    * @Param [scrollId]
    * @return com.mhuang.elk.common.source.crud.query.EsSearchBuilder
    */
    public ESSearchBuilder scrollId(String scrollId){
        this.queryContext.scrollId(scrollId);
        return this;
    }

    /**
    * @Title from
    * @Author admin
    * @Description 集合查询的开始值
    * @Date 14:37 2019/05/30
    * @Param [from]
    * @return com.mhuang.elk.common.source.crud.query.EsSearchBuilder
    */
    public ESSearchBuilder from(Integer from) {
        this.queryContext.from(from);
        return this;
    }

    /**
    * @Title scrollTimeout
    * @Author admin
    * @Description 分页Scroll查询的超时时间
    * @Date 14:37 2019/05/30
    * @Param [minute]
    * @return com.mhuang.elk.common.source.crud.query.EsSearchBuilder
    */
    public ESSearchBuilder scrollTimeout(long minute) {
        this.queryContext.scrollTimeout(minute);
        return this;
    }

    /**
    * @Title size
    * @Author admin
    * @Description 返回条数
    * @Date 14:38 2019/05/30
    * @Param [size]
    * @return com.mhuang.elk.common.source.crud.query.EsSearchBuilder
    */
    public ESSearchBuilder size(Integer size) {
         this.queryContext.size(size);
         return this;
    }

    /**
    * @Title indexNames
    * @Author admin
    * @Description 搜索的索引
    * @Date 14:38 2019/05/30
    * @Param [indexNames]
    * @return com.mhuang.elk.common.source.crud.query.EsSearchBuilder
    */
    public ESSearchBuilder indexNames(String[] indexNames) {
        this.queryContext.indexNames(indexNames);
        return this;
    }

    /**
    * @Title excludeFields
    * @Author admin
    * @Description 查询排除字段
    * @Date 14:38 2019/05/30
    * @Param [excludeFields]
    * @return com.mhuang.elk.common.source.crud.query.EsSearchBuilder
    */
    public ESSearchBuilder excludeFields(String[] excludeFields) {
        this.queryContext.excludeFields(excludeFields);
        return this;
    }

    /**
    * @Title includeFields
    * @Author admin
    * @Description 查询包含字段
    * @Date 14:39 2019/05/30
    * @Param [includeFields]
    * @return com.mhuang.elk.common.source.crud.query.EsSearchBuilder
    */
    public ESSearchBuilder includeFields(String[] includeFields) {
        this.queryContext.includeFields(includeFields);
        return this;
    }

    /**
    * @Title order
    * @Author admin
    * @Description 排序
    * @Date 14:39 2019/05/30
    * @Param [field, orderType]
    * @return com.mhuang.elk.common.source.crud.query.EsSearchBuilder
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

    public QueryContext getQueryContext() {
        return queryContext;
    }
}
