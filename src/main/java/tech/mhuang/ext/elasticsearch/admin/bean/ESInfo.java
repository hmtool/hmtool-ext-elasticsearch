package tech.mhuang.ext.elasticsearch.admin.bean;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * es 实体类
 *
 * @author mhuang
 * @since 1.0.0
 */
@Data
public class ESInfo {

    /**
     * 可指定多个key,value
     */
    private Map<String, ESBean> beanMap = new ConcurrentHashMap<>();

    @Data
    public static class ESBean {

        /**
         * 是否启动
         */
        private boolean enable;

        /**
         * 方式(http or https) 默认http
         */
        private String scheme = "http";
        /**
         * 设置es的ip或者是对应name 默认127.0.0.1本地
         */
        private String ip = "127.0.0.1";

        /**
         * 端口 默认9200
         */
        private Integer port = 9200;

        /**
         * 客户端连接数、默认20
         */
        private Integer connectNum = 20;

        /**
         * 并发数、默认10
         */
        private Integer connectPerRoute = 10;

        /**
         * 连接超时 默认10000
         */
        private Integer connectionTimeout = 10000;
        /**
         * 连接请求超时时间、默认3秒
         */
        private Integer connectionRequestTimeout = 3000;

        /**
         * socket超时时间 默认40秒
         */
        private Integer socketTimeout = 40000;
    }
}
