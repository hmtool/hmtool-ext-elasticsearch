package tech.mhuang.ext.elasticsearch.server.query;

import org.elasticsearch.client.RestHighLevelClient;
/**
 *
 * 扩展查询类
 *
 * @author zhangxh
 * @since 1.0.0
 */
public abstract  class AbstractESQuery {

    protected  RestHighLevelClient client;

    /**
     * 包装RestHighLevelClient
     * @param client
     */
    public AbstractESQuery(RestHighLevelClient client){
          this.client = client;
    }

    /**
     * 查询抽象
     * @param queryContext 查询全文
     * @return
     */
    public abstract ESQueryAware query(QueryContext queryContext);
}
