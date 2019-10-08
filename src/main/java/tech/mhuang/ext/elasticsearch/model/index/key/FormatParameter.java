package tech.mhuang.ext.elasticsearch.model.index.key;

public class FormatParameter {
    /**
     * 类型名称
     */
    public static final String NAME="format";

    /**
     * 可选值
     */
    public static enum Values{
        DATE("yyyy-MM-dd"),DATE_TIME("yyyy-MM-dd HH:mm:ss");
        private String value;
        Values(String value){
            this.value = value;
        }
        @Override
        public String toString() {
            return this.value;
        }
    }
}
