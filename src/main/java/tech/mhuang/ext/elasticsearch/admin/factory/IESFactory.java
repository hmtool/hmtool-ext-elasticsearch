package tech.mhuang.ext.elasticsearch.admin.factory;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import tech.mhuang.ext.elasticsearch.model.index.IndexProperties;
import tech.mhuang.ext.elasticsearch.server.query.AbstractESQuery;
import tech.mhuang.ext.elasticsearch.server.query.ESSearchBuilder;

import java.io.IOException;

/**
 * es接口层
 *
 * @author mhuang
 * @since 1.0.0
 */
public interface IESFactory {

    void setName(String name);
    void setClient(RestHighLevelClient client);
    /**
     * 获取构造器
     *
     * @return
     */
    ESSearchBuilder getBuilder();

    /**
     * 获取es查询实现类
     *
     * @return
     */
    AbstractESQuery getQuery();



    /**
     * 新增
     *
     * @param t 新增的数据
     * @return String 返回对应的id
     */
    <T> IndexResponse insert(T t) throws IOException;

    /**
     * 新增
     *
     * @param data  新增的数据
     * @param index 新增的数据中的索引
     * @param type  新增的数据中的类型
     * @return String 返回id
     */
    <T> IndexResponse insert(T data, String index, String type) throws IOException;

    /**
     * 新增
     *
     * @param data  新增的数据
     * @param index 新增的数据中的索引
     * @param type  新增的数据中的类型
     * @return String 返回id
     */
    IndexResponse insert(String data, String index, String type) throws IOException;

    /**
     * 修改
     *  @param t  修改的数据
     * @param id 修改的id
     * @return
     */
    <T> UpdateResponse update(T t, String id) throws IOException;

    /**
     * 数据更新
     *  @param data  更新的数据
     * @param index 更新的索引
     * @param type  更新的类型
     * @param id    更新的id
     * @return
     */
    UpdateResponse update(String data, String index, String type, String id) throws IOException;

    /**
     * 数据更新
     *  @param data  更新的数据
     * @param index 更新的索引
     * @param type  更新的类型
     * @param id    更新的id
     * @return
     */
    <T> UpdateResponse update(T data, String index, String type, String id) throws IOException;

    /**
     * 删除索引及数据
     * @param index 索引
     * @return
     * @throws IOException
     */
    AcknowledgedResponse delete(String index) throws IOException;
    /**
     *
     * 通过id删除数据
     *
     * @param index 删除的索引
     * @param type 删除的索引类型
     * @param id   删除的id
     * @return
     * @throws IOException
     */
    DeleteResponse delete(String index, String type, String id) throws IOException;
    /**
     * 获取链接
     *
     * @return TransportClient 返回链接对象
     */
    RestHighLevelClient getClient();

    /**
     * 更新索引属性
     *
     * @param index      更新的索引
     * @param type       更新的索引类型
     * @param properties 更新的属性
     */
    AcknowledgedResponse updateIndexProperties(String index, String type, IndexProperties properties) throws Exception;
}
