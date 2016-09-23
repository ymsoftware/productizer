package productizer.elasticsearch;

import org.elasticsearch.client.Client;

/**
 * Created by ymetelkin on 9/23/16.
 */
public abstract class ElasticsearchRequestBase<T> {
    private Client client;
    private String index;
    private String type;

    public Client getClient() {
        return this.client;
    }

    public ElasticsearchRequestBase setClient(Client client) {
        this.client = client;
        return this;
    }

    public String getIndex() {
        return this.index;
    }

    public ElasticsearchRequestBase setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public ElasticsearchRequestBase setType(String type) {
        this.type = type;
        return this;
    }

    public abstract T getResponse();
}
