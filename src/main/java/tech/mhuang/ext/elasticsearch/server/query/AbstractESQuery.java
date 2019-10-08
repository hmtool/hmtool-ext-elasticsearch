package tech.mhuang.ext.elasticsearch.server.query;

import org.elasticsearch.client.RestHighLevelClient;
/**
 * @ClassName AbstractEsQuery
 * @Description: 查询抽象类
 * @Author admin
 * @Date 2019/05/30 9:30
 * @Version 0.0.3.3.2
 */
public abstract  class AbstractESQuery {

    protected  RestHighLevelClient client;

    public AbstractESQuery(RestHighLevelClient client){
          this.client = client;
    }

    public abstract ESQueryAware query(QueryContext queryContext);
}
