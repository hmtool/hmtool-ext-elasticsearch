package tech.mhuang.ext.elasticsearch.server.query;

import tech.mhuang.ext.elasticsearch.model.query.ESOrder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 可执行的上下文类
 *
 * @author zhangxh
 * @since 1.0.0
 */
@Getter
public class QueryContext {

    public static  final long SCROLL_TIMEOUT_DEFAULT=30L;

    /**
     * 每次显示行数
     */
    private Integer size;

    private Integer from;

    /**
     * 分钟数
     */
    private Long scrollTimeout;

    private String scrollId;

    private String[] indexNames;

    private String[] includeFields;

    private String[] excludeFields;

    private List<ESOrder> orders = new ArrayList<>(0);

    /**
     * 查询语法存储
     */
     List<OperatorContext> contexts = new ArrayList<>(0);

     void addContext(OperatorContext context){
        contexts.add(context);
    }

     void scrollId(String scrollId) {
        this.scrollId = scrollId;
    }

     void from(Integer from) {
        this.from = from;
    }

     void size(Integer size) {
        this.size = size;
    }

     void indexNames(String[] indexNames) {
        this.indexNames = indexNames;
    }

     void includeFields(String[] includeFields) {
        this.includeFields = includeFields;
    }

     void excludeFields(String[] excludeFields) {
        this.excludeFields = excludeFields;
    }

     void addOrder(ESOrder order) {
        orders.add(order);
    }

     void scrollTimeout(long minute) {
         this.scrollTimeout = minute;
    }
}
