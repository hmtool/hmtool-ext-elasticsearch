package tech.mhuang.ext.elasticsearch.server.query;

import lombok.Data;

/**
 *
 * 查询条件封装类
 *
 * @author zhangxh
 * @since 1.0.0
 */
@Data
public class OperatorContext {

    /**
     * 语法
     */
    private QueryQuota queryQuota;

    /**
     * 域
     */
    private String fieldName;

    /**
     * 值
     */
    private Object[] values;

    /**
     * 操作类型
     */
    private OperatorType operateType;
}
