package tech.mhuang.ext.elasticsearch.server.query;

/**
 *
 * 查询语法
 *
 * @author zhangxh
 * @since 1.0.0
 */
public enum  QueryQuota {
    /**
     * and 并且
     */
     AND("and"),
    /**
     * OR符号
     */
    OR("or"),
    /**
     * 左括号
     */
    LT("("),
    /**
     * 右括号
     */
    GT(")"),
    /**
     * CONDITION
     */
    CONDITION("");

    private String message;

    QueryQuota(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
