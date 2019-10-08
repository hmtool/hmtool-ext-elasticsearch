package tech.mhuang.ext.elasticsearch.model.query;

import lombok.Data;

import java.util.List;

/**
 * 分页信息
 */
@Data
public class ESPage<T> {
    /**
     * 总数
     */
    private long total;

    /**
     * scrollId
     */
    private String scrollId;

    /**
     * 当前行数
     */
    private List<T> rows;


}
