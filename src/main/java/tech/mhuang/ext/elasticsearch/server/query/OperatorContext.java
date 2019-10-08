package tech.mhuang.ext.elasticsearch.server.query;

import lombok.Data;
/**
 * @ClassName OperateContext
 * @Description: 查询条件封装类
 * @Author admin
 * @Date 2019/05/30 9:33
 * @Version 0.0.3.3.2
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
