package tech.mhuang.ext.elasticsearch.server.query;

import tech.mhuang.ext.elasticsearch.model.query.ESPage;

import java.util.List;

/**
 * 查询类
 */
public interface ESQueryAware {

     <T>List<T> list(Class<T> clz);

     <T> ESPage<T> page(Class<T> clz);

     <T> T single(Class<T> clz);

}
