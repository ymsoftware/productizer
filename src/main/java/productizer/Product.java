package productizer;

import java.util.Map;

/**
 * Created by ymetelkin on 9/21/16.
 */
public class Product {
    private long id;
    private long appId;
    private String name;
    private Map<String, Object> query;

    public long getId() {
        return this.id;
    }

    public Product setId(long id) {
        this.id = id;
        return this;
    }

    public long getAppId() {
        return this.appId;
    }

    public Product setAppId(long appId) {
        this.appId = appId;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public Map<String, Object> getQuery() {
        return this.query;
    }

    public Product setQuery(Map<String, Object> query) {
        this.query = query;
        return this;
    }
}