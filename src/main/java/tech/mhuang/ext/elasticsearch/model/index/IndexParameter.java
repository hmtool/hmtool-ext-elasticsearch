package tech.mhuang.ext.elasticsearch.model.index;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class IndexParameter {

    private Map<String,String> parameters = new HashMap<>();

    /**
     * 获取实例
     * @return IndexParameter
     */
    public static IndexParameter getInstance(){
        return new IndexParameter();
    }


    /**
     * 写入参数
     * @param key
     * @param value
     * @return IndexParameter
     */
    public  IndexParameter addKey(String key,String value){
        parameters.put(key,value);
        return this;
    }

    /**
     * 移除参数
     * @param key
     * @return IndexProperties
     */
    public  IndexParameter removeKey(String key){
        parameters.remove(key);
        return this;
    }



}
