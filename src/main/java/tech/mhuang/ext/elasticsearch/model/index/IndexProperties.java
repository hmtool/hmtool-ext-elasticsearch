package tech.mhuang.ext.elasticsearch.model.index;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class IndexProperties {

    private Map<String,IndexParameter> properties = new HashMap<>();

    /**
     * 获取实例
     * @return IndexProperties
     */
    public static IndexProperties getInstance(){
         return new IndexProperties();
    }


    /**
     * 写入参数
     * @param key 写入的key
     * @param parameter 写入的参数
     * @return IndexProperties
     */
    public  IndexProperties addKey(String key,IndexParameter parameter){
        properties.put(key,parameter);
        return this;
    }

    /**
     * 移除参数
     * @param key 移除的key
     * @return IndexProperties
     */
    public  IndexProperties removeKey(String key){
        properties.remove(key);
        return this;
    }
}
