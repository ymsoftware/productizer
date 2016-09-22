import org.junit.Assert;
import org.junit.Test;
import productizer.nginx.NginxRequestHandler;
import productizer.nginx.NginxResponses;
import productizer.nginx.NginxRoutingHandlerBase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static nginx.clojure.MiniConstants.*;
import static productizer.nginx.NginxRoutingHandlerBase.REQUEST_ARGS;

/**
 * Created by ymetelkin on 9/22/16.
 */
public class NginxTests {
    @Test
    public void routeTests() throws IOException {
        class NginxTestRequestHandler implements NginxRequestHandler {
            @Override
            public Object[] invoke(Map<String, Object> request) {
                Map<String, Object> args = (Map<String, Object>) request.getOrDefault(REQUEST_ARGS, null);
                if (args == null) {
                    return NginxResponses.json("{\"status\":\"success\"}");
                } else {
                    return NginxResponses.json(String.format("{\"result\":\"%s\"}", args.getOrDefault("id", "N/A")));
                }
            }
        }

        class NginxTestRoutingHandler extends NginxRoutingHandlerBase {
            public NginxTestRoutingHandler() {
                get("/api/test/route", new NginxTestRequestHandler());
            }
        }

        class NginxTestRoutingHandler2 extends NginxRoutingHandlerBase {
            public NginxTestRoutingHandler2() {
                super("/api/v2/");
                get("/test/route", new NginxTestRequestHandler());
                get("/test/route/{id}", new NginxTestRequestHandler());
                put("/test/route/{id}/{action}/xyz/{abc}", new NginxTestRequestHandler());
            }
        }

        NginxRoutingHandlerBase routing = new NginxTestRoutingHandler();

        Map<String, Object> request = new HashMap<>();
        request.put(URI, "/api/test/route/");
        request.put(REQUEST_METHOD, GET);
        Object[] response = routing.invoke(request);
        Assert.assertTrue((int) response[0] == NGX_HTTP_OK);

        request.put(REQUEST_METHOD, PUT);
        response = routing.invoke(request);
        Assert.assertTrue((int) response[0] == NGX_HTTP_NOT_ALLOWED);

        request.put(URI, "/api/test/route/1");
        response = routing.invoke(request);
        Assert.assertTrue((int) response[0] == NGX_HTTP_NOT_FOUND);

        routing = new NginxTestRoutingHandler2();

        request.put(URI, "/api/v2/test/route/");
        request.put(REQUEST_METHOD, GET);
        response = routing.invoke(request);
        Assert.assertTrue((int) response[0] == NGX_HTTP_OK);

        request.put(URI, "/api/v2/test/route/123");
        response = routing.invoke(request);
        Assert.assertTrue((int) response[0] == NGX_HTTP_OK);
        Assert.assertTrue(((String) response[2]).indexOf("123") > 0);

        request.put(URI, "/api/v2/test/route/123/update/xyz/ym");
        request.put(REQUEST_METHOD, PUT);
        response = routing.invoke(request);
        Assert.assertTrue((int) response[0] == NGX_HTTP_OK);
        Assert.assertTrue(((String) response[2]).indexOf("123") > 0);
    }
}
