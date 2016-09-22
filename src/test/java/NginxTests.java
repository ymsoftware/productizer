import nginx.clojure.java.NginxJavaRingHandler;
import org.junit.Assert;
import org.junit.Test;
import productizer.nginx.NginxProductizerRoutingHandler;
import productizer.nginx.NginxRequestHandler;
import productizer.nginx.NginxResponses;
import productizer.nginx.NginxRoutingHandlerBase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static nginx.clojure.MiniConstants.*;

/**
 * Created by ymetelkin on 9/22/16.
 */
public class NginxTests {
    @Test
    public void routeTests() throws IOException {
        class NginxTestRequestHandler implements NginxRequestHandler {
            @Override
            public Object[] invoke(Map<String, Object> map) {
                return NginxResponses.json("{\"status\":\"success\"}");
            }
        }

        class NginxTestRoutingHandler extends NginxRoutingHandlerBase {
            public NginxTestRoutingHandler() {
                get("/api/test/route", new NginxTestRequestHandler());
            }
        }

        NginxRoutingHandlerBase routing = new NginxTestRoutingHandler();

        Map<String, Object> request = new HashMap<>();
        request.put(URI, "/api/test/route/");
        request.put(REQUEST_METHOD, GET);
        Object[] response = routing.invoke(request);
        Assert.assertTrue((int)response[0] == NGX_HTTP_OK);

        request.put(REQUEST_METHOD, PUT);
        response = routing.invoke(request);
        Assert.assertTrue((int)response[0] == NGX_HTTP_NOT_ALLOWED);

        request.put(URI, "/api/test/route/1");
        response = routing.invoke(request);
        Assert.assertTrue((int)response[0] == NGX_HTTP_NOT_FOUND);
    }
}
