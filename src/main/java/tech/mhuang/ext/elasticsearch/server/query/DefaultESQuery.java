package tech.mhuang.ext.elasticsearch.server.query;

import com.alibaba.fastjson.JSON;
import tech.mhuang.ext.elasticsearch.model.query.ESPage;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 默认的查询实现
 *
 * @author zhangxh
 * @since 1.0.0
 */
@Slf4j
public class DefaultESQuery extends AbstractESQuery {

    public DefaultESQuery(RestHighLevelClient client) {
        super(client);
    }

    @Override
    public ESQueryAware query(QueryContext queryContext) {
        return new DefaultEsQueryResult(queryContext,client);
    }

    class DefaultEsQueryResult extends AbstractESQueryAware {

         private QueryContext queryContext = null;

         private RestHighLevelClient client = null;

         private  SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

         private SearchRequest searchRequest = new SearchRequest();

         private SearchScrollRequest searchScrollRequest  = new SearchScrollRequest ();

         DefaultEsQueryResult(QueryContext queryContext,RestHighLevelClient client){
              this.queryContext = queryContext;
              this.client = client;
          }

            @Override
            public <T> List<T> list(Class<T> clz)  {
                SearchResponse response = null;
                try {
                    List<T> results = new ArrayList<>();
                    this.analizeNotScollContext(this.searchRequest,this.searchSourceBuilder,this.queryContext);
                    if(queryContext.getSize()==null){
                        searchSourceBuilder.size(10000);
                        log.warn("No size is set, the system defaults to  maximum 10000");
                    }
                    searchRequest.source(searchSourceBuilder);
                    response = client.search(searchRequest, RequestOptions.DEFAULT);
                    SearchHits hits=response.getHits();
                    if(hits !=null && hits.getHits()!=null && hits.getHits().length>0){
                        for (SearchHit searchHit : response.getHits()) {
                            T obj = JSON.parseObject(searchHit.getSourceAsString(),clz);
                            results.add(obj);
                        }
                    }
                    return results;
                } catch (Exception e) {
                   throw new ElasticsearchException(e);
                }finally {
                }
            }

            @Override
            public <T> ESPage<T> page(Class<T> clz) {
                ESPage<T> esPage = new ESPage<>();
                SearchResponse response = null;
                try {
                    String scrollId = null;
                    SearchHits hits = null;
                    if(queryContext.getScrollId()==null){
                        this.analizeNotScollContext(this.searchRequest,this.searchSourceBuilder,this.queryContext);
                        searchRequest.source(searchSourceBuilder);
                        this.analizeFirstScollContext(this.searchRequest,this.queryContext);
                        response = client.search(searchRequest, RequestOptions.DEFAULT);
                        scrollId = response.getScrollId();
                        hits = response.getHits();
                    }else{
                        this.analizeScollContext(this.searchScrollRequest,this.queryContext);
                        SearchScrollRequest scrollRequest = new SearchScrollRequest(queryContext.getScrollId());
                        SearchResponse searchScrollResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
                        scrollId = searchScrollResponse.getScrollId();
                        hits = searchScrollResponse.getHits();
                    }
                    long totalHits =  hits.totalHits;
                    esPage.setTotal(totalHits);
                    List<T> results = new ArrayList<>();
                    if(hits !=null && hits.getHits()!=null && hits.getHits().length>0){
                        for (SearchHit searchHit : hits) {
                            T obj = JSON.parseObject(searchHit.getSourceAsString(),clz);
                            results.add(obj);
                        }
                    }
                    esPage.setScrollId(scrollId);
                    esPage.setRows(results);
                    return esPage;
                } catch (Exception e) {
                    throw new ElasticsearchException(e);
                }finally {
                }
            }

            @Override
            public <T> T single(Class<T> clz) {
                this.queryContext.size(1);
                List<T> results = this.list(clz);
                return results.size() == 0?null:results.get(0);
            }
    }
}
