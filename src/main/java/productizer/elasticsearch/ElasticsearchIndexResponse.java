package productizer.elasticsearch;

import org.elasticsearch.action.DocWriteResponse;

/**
 * Created by ymetelkin on 9/23/16.
 */
public class ElasticsearchIndexResponse extends ElasticsearchResponseBase<DocWriteResponse> {
    private long version;

    public long getVersion() {
        return this.version;
    }

    public ElasticsearchIndexResponse setVersion(long version) {
        this.version = version;
        return this;
    }
}
