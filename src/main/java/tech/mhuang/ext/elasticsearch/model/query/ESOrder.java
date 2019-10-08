package tech.mhuang.ext.elasticsearch.model.query;

import tech.mhuang.ext.elasticsearch.server.query.OrderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ESOrder {

    private String fieldName;

    private OrderType orderType;
}