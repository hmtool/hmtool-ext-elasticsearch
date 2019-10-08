package tech.mhuang.ext.elasticsearch.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.mhuang.core.check.CheckAssert;
import tech.mhuang.ext.elasticsearch.admin.factory.IESFactory;
import tech.mhuang.ext.elasticsearch.model.index.IndexProperties;
import tech.mhuang.ext.elasticsearch.server.annoation.ESTable;
import tech.mhuang.ext.elasticsearch.server.query.AbstractESQuery;
import tech.mhuang.ext.elasticsearch.server.query.DefaultESQuery;
import tech.mhuang.ext.elasticsearch.server.query.ESSearchBuilder;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * es工厂实现类
 *
 * @author mhuang
 * @since 1.0.0
 */
public class ESFactory implements IESFactory {

    private String name;

    private RestHighLevelClient client;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setClient(RestHighLevelClient client) {
        this.client = client;
    }

    @Override
    public ESSearchBuilder getBuilder() {
        return new ESSearchBuilder();
    }

    @Override
    public AbstractESQuery getQuery() {
        return new DefaultESQuery(this.client);
    }


    /**
     * 插入
     *
     * @param t 插入的数据
     * @return
     */
    @Override
    public <T> IndexResponse insert(T t) throws IOException {
        ESTable esTable = checkESTable(t);
        return insert(t, esTable.index(), esTable.type());
    }

    @Override
    public <T> IndexResponse insert(T t, String index, String type) throws IOException {
        return insert(packEntity(t), index, type);
    }

    @Override
    public IndexResponse insert(String data, String index, String type) throws IOException {
        return baseInsert(data, index, type);
    }

    private <T> String packEntity(T t) {
        return JSON.toJSONString(t);
    }

    private IndexResponse baseInsert(String data, String index, String type) throws IOException {
        logger.debug("===正在插入ES数据=====");
        logger.debug("放入得ES数据为{}", data);
        JSONObject jsonObject = JSONObject.parseObject(data);
        IndexRequest indexRequest = new IndexRequest(index, type).source(jsonObject);

        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
        logger.debug("===放入ES数据完毕=====,response:{}", response);
        return response;
    }

    private <T> ESTable checkESTable(T model) {
        ESTable esTable = null;
        if (model.getClass().isAnnotationPresent(ESTable.class)) {
            esTable = model.getClass().getAnnotation(ESTable.class);
        }
        return CheckAssert.check(esTable, "当前保存的对象不是ES对象");
    }

    /**
     * ES更新
     *
     * @param t 更新的对象
     * @return
     * @see IESFactory#update(java.lang.Object, java.lang.String)
     */
    @Override
    public <T> UpdateResponse update(T t, String id) throws IOException {
        ESTable esTable = checkESTable(t);
        return update(t, esTable.index(), esTable.type(), id);
    }

    @Override
    public <T> UpdateResponse update(T t, String index, String type, String id) throws IOException {
        return update(packEntity(t), index, type, id);
    }

    @Override
    public AcknowledgedResponse delete(String index) throws IOException {
        logger.debug("===正在删除ES数据=====,index:{}", index);
        DeleteIndexRequest deleteRequest = new DeleteIndexRequest(index);
        AcknowledgedResponse response = client.indices().delete(deleteRequest, RequestOptions.DEFAULT);
        logger.debug("===删除ES数据完毕=====,response:{}", response);
        return response;
    }

    @Override
    public DeleteResponse delete(String index, String type, String id) throws IOException {
        logger.debug("===正在删除ES数据=====,index:{},type:{},id:{}", index, type, id);
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        logger.debug("===删除ES数据完毕=====,response:{}", response);
        return response;
    }

    @Override
    public UpdateResponse update(String data, String index, String type, String id) throws IOException {
        return baseUpdate(data, index, type, id);
    }

    private UpdateResponse baseUpdate(String data, String index, String type, String id) throws IOException {
        logger.debug("===正在修改ES数据=====");
        logger.debug("修改ES数据为{}，id为:{}", data, id);
        JSONObject jsonObject = JSONObject.parseObject(data);
        UpdateRequest updateRequest = new UpdateRequest(index, type, id)
                .doc(jsonBuilder().map(jsonObject));
        UpdateResponse response = getClient().update(updateRequest, RequestOptions.DEFAULT);
        logger.debug("打印应答的数据是:{}", response);
        return response;

    }

    @Override
    public RestHighLevelClient getClient() {
        return client;
    }

    /**
     * 更新索引属性
     */
    @Override
    public AcknowledgedResponse updateIndexProperties(String index, String type, IndexProperties properties) throws Exception {
        PutMappingRequest request = new PutMappingRequest(index);
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                properties.getProperties().forEach((k, v) -> {
                    try {
                        builder.startObject(k);
                        {
                            v.getParameters().forEach((ik, iv) -> {
                                try {
                                    builder.field(ik, iv);
                                } catch (IOException e) {
                                    logger.error(e.getMessage(), e);
                                }
                            });
                        }
                        builder.endObject();
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                });
            }
            builder.endObject();
        }
        builder.endObject();
        request.type(type);
        request.source(builder);
        AcknowledgedResponse response = this.getClient().indices().putMapping(request, RequestOptions.DEFAULT);
        logger.debug("打印应答的数据是:{}", response);
        return response;
    }
}
