package tech.mhuang.ext.elasticsearch.admin;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import tech.mhuang.ext.elasticsearch.admin.bean.ESInfo;
import tech.mhuang.ext.elasticsearch.admin.external.IESExternal;
import tech.mhuang.ext.elasticsearch.admin.factory.IESFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * es平台实现
 *
 * @author mhuang
 * @since 1.0.0
 */
@Slf4j
public class ESFramework {

    /**
     * elasticsearch配置信息
     */
    private ESInfo info;

    /**
     * elasticsearch external
     */
    private IESExternal external;

    private Map<String,IESFactory> factory = new ConcurrentHashMap<>();

    /**
     * 获取容器
     * @param key 获取容器的key
     * @return IESFactory
     */
    public IESFactory getFactory(String key){
        return this.factory.get(key);
    }

    public ESFramework(ESInfo info) {
        this.info = info;
    }

    public ESFramework external(IESExternal external){
        this.external = external;
        return this;
    }
    /**
     * 启动
     */
    public void start() {
        if(this.external == null){
            this.external = new IESExternal() {
            };
        }
        log.info("正在启动elasticsearch...");
        log.info("正在装载elasticsearch配置,{}", info);
        info.getBeanMap().forEach((key,bean)->{
            //配置开启才加载
            if(bean.isEnable()){
                RestHighLevelClient client = loadProperties(bean);
                IESFactory esFactory = external.create(key);
                esFactory.setClient(client);
                esFactory.setName(key);
                factory.put(key,esFactory);
            }
        });
        log.info("elasticsearch 配置装载完成...");
        log.info("elasticsearch 启动成功");
    }

    /**
     * 装载配置
     */
    private RestHighLevelClient loadProperties(ESInfo.ESBean bean) {
        HttpHost httpHost = new HttpHost(bean.getIp(), bean.getPort(), bean.getScheme());
        RestClientBuilder builder = RestClient.builder(httpHost);
        setterClientConfig(builder,bean);
        return new RestHighLevelClient(builder);
    }

    /**
     * 设置客户端配置
     */
    private void setterClientConfig(RestClientBuilder builder,ESInfo.ESBean bean){
        builder.setRequestConfigCallback((RequestConfig.Builder requestConfigBuilder) -> {
            requestConfigBuilder.setConnectTimeout(bean.getConnectionTimeout());
            requestConfigBuilder.setSocketTimeout(bean.getSocketTimeout());
            requestConfigBuilder.setConnectionRequestTimeout(bean.getConnectionRequestTimeout());
            return requestConfigBuilder;
        });
        builder.setHttpClientConfigCallback((HttpAsyncClientBuilder httpClientBuilder) -> {
            httpClientBuilder.setMaxConnTotal(bean.getConnectNum());
            httpClientBuilder.setMaxConnPerRoute(bean.getConnectPerRoute());
            return httpClientBuilder;
        });
    }
}