package productizer.nginx;

/**
 * Created by ymetelkin on 9/21/16.
 */
public class NginxProductizerRoutingHandler extends NginxRoutingHandlerBase {
    public NginxProductizerRoutingHandler() {
        super("api");

        put("products/{id}", new NginxProductIndexHandler());
        get("products/{id}", new NginxProductGetHandler());
        delete("products/{id}", new NginxProductDeleteHandler());
        get("products/search", new NginxProductSearchHandler());
    }
}
