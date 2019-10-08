package tech.mhuang.ext.elasticsearch.model.index.key;

public class AnalyzerParameter {
    /**
     * 类型名称
     */
    public static final String NAME="analyzer";

    /**
     * 可选值
     */
    public static enum Values{
        IkMaxWord("ik_max_word"),IkSmart("ik_smart"),English("english"),
        Simple("simple"),Whitespace("whitespace"),Stop("stop"),Keyword("keyword");
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
