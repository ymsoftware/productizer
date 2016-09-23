package productizer.elasticsearch;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.inject.util.Types;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by ymetelkin on 9/23/16.
 */
public abstract class ElasticsearchSearchRequestBase<T> extends ElasticsearchRequestBase<ElasticsearchSearchResponse<T>> {
    private String query;
    private int from;
    private int size;

    public String getQuery() {
        return this.query;
    }

    public ElasticsearchSearchRequestBase setQuery(String query) {
        this.query = query;
        return this;
    }

    public int getFrom() {
        return this.from;
    }

    public ElasticsearchSearchRequestBase setFrom(int from) {
        this.from = from;
        return this;
    }

    public int getSize() {
        return this.size;
    }

    public ElasticsearchSearchRequestBase setSize(int size) {
        this.size = size;
        return this;
    }

    @Override
    public ElasticsearchSearchResponse<T> getResponse() {
        ElasticsearchSearchResponse<T> response = new ElasticsearchSearchResponse<>();

        try {
            QueryBuilder qb = Strings.isNullOrEmpty(query) ?
                    QueryBuilders.matchAllQuery() :
                    QueryBuilders.queryStringQuery(query);

            SearchRequestBuilder request = this.getClient()
                    .prepareSearch(this.getIndex())
                    .setTypes(this.getType())
                    .setQuery(qb);

            if (from > 0) {
                request = request.setFrom(from);
            }

            if (size > 0) {
                request = request.setSize(size);
            }

            SearchResponse es = request.get();
            SearchHits hits = es.getHits();
            T[] items = parseItems(hits);
            long total = hits.getTotalHits();

            response
                    .setItems(items)
                    .setTotal(total)
                    .setSuccess(true)
                    .setResult(total > 0 ? "Found" : "Not items found")
                    .setElasticsearchResponse(es);

        } catch (Exception ex) {
            response.setResult(ex.getMessage());
        }

        return response;
    }

    public abstract T parseItem(String id, Map<String, Object> json);

    public T[] parseItems(SearchHits hits){
        T[] items = (T[]) Arrays.stream(hits.getHits())
                .map(hit -> parseItem(hit.getId(), hit.sourceAsMap()))
                .toArray();

        return items;
    }
}
