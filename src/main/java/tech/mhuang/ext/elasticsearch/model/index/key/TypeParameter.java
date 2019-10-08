package tech.mhuang.ext.elasticsearch.model.index.key;

public class TypeParameter{
    /**
     * 类型名称
     */
    public static final String NAME="type";

    /**
     * 可选值
     */
    public static enum Values{
        Text("text"),Keyword("keyword"),Long("long"),Integer("integer"),Short("short"),Byte("byte"),Double("double"),
        Float("float"),HalfFloat("half_float"),ScaledFloat("scaled_float"),Date("date"),Bollean("boolean"),Binary("binary")
        ,IntegerRange("integer_range"),FloatRange("float_range"),LongRange("long_range"),DoubleRange("double_range"),
        DateRange("date_range"),Object("object"),Nested("nested"),Ip("ip");
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
