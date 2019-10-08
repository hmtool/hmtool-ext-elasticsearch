package tech.mhuang.ext.elasticsearch.server.query;
/**
 * @ClassName QueryQuota
 * @Description: 查询语法
 * @Author admin
 * @Date 2019/05/30 9:34
 * @Version 0.0.3.3.2
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
