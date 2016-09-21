package productizer.nginx;

/**
 * Created by ymetelkin on 9/21/16.
 */
public class NginxProductizerRoutingHandler extends NginxRoutingHandlerBase {
    public NginxProductizerRoutingHandler() {
        get("/api/products", null);
    }
}
