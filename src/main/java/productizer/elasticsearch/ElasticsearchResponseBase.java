package productizer.elasticsearch;

/**
 * Created by ymetelkin on 9/23/16.
 */
public abstract class ElasticsearchResponseBase<T> {
    private boolean success;
    private String result;
    private T ElasticsearchResponse;

    public boolean isSuccess() {
        return this.success;
    }

    public ElasticsearchResponseBase setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getResult() {
        return this.result;
    }

    public ElasticsearchResponseBase setResult(String result) {
        this.result = result;
        return this;
    }

    public T getElasticsearchResponse() {
        return this.ElasticsearchResponse;
    }

    public ElasticsearchResponseBase setElasticsearchResponse(T elasticsearchResponse) {
        ElasticsearchResponse = elasticsearchResponse;
        return this;
    }

    public void error(String error) {
        this.success = false;
        this.result = error;
    }
}
