package productizer.elasticsearch;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.common.Strings;

import java.util.Map;

/**
 * Created by ymetelkin on 9/23/16.
 */
public abstract class ElasticsearchGetRequestBase<T> extends ElasticsearchRequestBase<ElasticsearchGetResponse<T>> {
    private String id;

    public ElasticsearchGetRequestBase(String id) {
        this.id = id;
    }

    @Override
    public ElasticsearchGetResponse<T> getResponse() {
        ElasticsearchGetResponse<T> response = new ElasticsearchGetResponse();

        if (Strings.isNullOrEmpty(this.id)) {
            response.error("Missing item ID");
        } else {
            try {
                GetResponse es = this.getClient()
                        .prepareGet()
                        .setIndex(this.getIndex())
                        .setType(this.getType())
                        .setId(this.id)
                        .get();

                boolean found = es.isExists();
                if (found) {
                    T item = parseItem(this.id, es.getSourceAsMap());
                    response.setItem(item);
                }

                response
                        .setExists(found)
                        .setSuccess(true)
                        .setResult(found ? "Found" : String.format("Item %s is not found", this.id))
                        .setElasticsearchResponse(es);
            } catch (Exception ex) {
                response.setResult(ex.getMessage());
            }
        }

        return response;
    }

    public abstract T parseItem(String id, Map<String, Object> json);
}
