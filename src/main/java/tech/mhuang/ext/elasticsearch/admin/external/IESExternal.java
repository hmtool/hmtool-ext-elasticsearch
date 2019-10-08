package tech.mhuang.ext.elasticsearch.admin.external;

import tech.mhuang.ext.elasticsearch.server.ESFactory;
import tech.mhuang.ext.elasticsearch.admin.factory.IESFactory;

/**
 * @package: tech.mhuang.ext.elasticsearch.admin.external
 * @author: mhuang
 * @Date: 2019/9/16 14:33
 * @Description:
 */
public interface IESExternal {

    /**
     * 创建
     * @return
     */
    default IESFactory create(String key){
        return new ESFactory();
    }
}