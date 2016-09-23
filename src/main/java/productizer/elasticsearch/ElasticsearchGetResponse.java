package productizer.elasticsearch;

import org.elasticsearch.action.get.GetResponse;

/**
 * Created by ymetelkin on 9/23/16.
 */
public class ElasticsearchGetResponse<T> extends ElasticsearchResponseBase<GetResponse> {
    private boolean exists;
    private long version;
    private T item;

    public boolean isExists() {
        return this.exists;
    }

    public ElasticsearchGetResponse setExists(boolean exists) {
        this.exists = exists;
        return this;
    }

    public long getVersion() {
        return this.version;
    }

    public ElasticsearchGetResponse setVersion(long version) {
        this.version = version;
        return this;
    }

    public T getItem() {
        return this.item;
    }

    public ElasticsearchGetResponse setItem(T item) {
        this.item = item;
        return this;
    }
}
