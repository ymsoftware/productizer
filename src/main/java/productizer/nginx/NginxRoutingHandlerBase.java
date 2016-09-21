package productizer.nginx;

import nginx.clojure.java.NginxJavaRingHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static nginx.clojure.MiniConstants.*;

/**
 * Created by ymetelkin on 9/21/16.
 */
public abstract class NginxRoutingHandlerBase implements NginxJavaRingHandler {
    Map<Object, Map<Object, NginxJavaRingHandler>> routes;

    @Override
    public Object[] invoke(Map<String, Object> request) throws IOException {
        Object uri = request.get(URI);

        if (this.routes == null) {
            return NginxUtils.invalidUrlResponse((String) uri);
        }

        Map<Object, NginxJavaRingHandler> methods = this.routes.getOrDefault(uri, null);
        if (methods == null) {
            return NginxUtils.invalidUrlResponse((String) uri);
        }

        Object method = request.get(REQUEST_METHOD);
        NginxJavaRingHandler handler = methods.getOrDefault(method, null);
        if (handler == null) {
            return NginxUtils.invalidUrlMethodResponse((String) uri, (String) method);
        }

        return handler.invoke(request);
    }

    void get(String url, NginxJavaRingHandler handler) {
        addHandler(url, GET, handler);
    }

    void post(String url, NginxJavaRingHandler handler) {
        addHandler(url, POST, handler);
    }

    void put(String url, NginxJavaRingHandler handler) {
        addHandler(url, PUT, handler);
    }

    void delete(String url, NginxJavaRingHandler handler) {
        addHandler(url, DELETE, handler);
    }

    void head(String url, NginxJavaRingHandler handler) {
        addHandler(url, HEAD, handler);
    }

    private void addHandler(String url, String method, NginxJavaRingHandler handler) {
        if (this.routes == null) {
            this.routes = new HashMap<>();
        }

        Map<Object, NginxJavaRingHandler> methods = this.routes.getOrDefault(url, null);
        if (methods == null) {
            methods = new HashMap<>();
        }

        methods.put(method, handler);
        this.routes.put(url, methods);
    }
}
