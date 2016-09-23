package productizer.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;

/**
 * Created by ymetelkin on 9/23/16.
 */
public class ElasticsearchSearchResponse<T> extends ElasticsearchResponseBase<SearchResponse> {
    private T[] items;
    private long total;

    public T[] getItems() {
        return this.items;
    }

    public ElasticsearchSearchResponse setItems(T[] items) {
        this.items = items;
        return this;
    }

    public long getTotal() {
        return this.total;
    }

    public ElasticsearchSearchResponse setTotal(long total) {
        this.total = total;
        return this;
    }
}
