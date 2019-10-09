package tech.mhuang.ext.elasticsearch.admin.external;

import tech.mhuang.ext.elasticsearch.server.ESFactory;
import tech.mhuang.ext.elasticsearch.admin.factory.IESFactory;

/**
 *
 * es扩展
 *
 * @author mhuang
 * @since 1.0.0
 */
public interface IESExternal {

    /**
     * 创建
     * @param key
     * @return
     */
    default IESFactory create(String key){
        return new ESFactory();
    }
}