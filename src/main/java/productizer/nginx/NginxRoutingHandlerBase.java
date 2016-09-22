package productizer.nginx;

import nginx.clojure.java.NginxJavaRingHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nginx.clojure.MiniConstants.*;
import static productizer.utils.StringUtils.isEmptyOrNull;

/**
 * Created by ymetelkin on 9/21/16.
 */
public abstract class NginxRoutingHandlerBase implements NginxJavaRingHandler {
    Map<String, NginxRouteToken> routes;

    @Override
    public Object[] invoke(Map<String, Object> request) throws IOException {
        String url = (String) request.get(URI);

        if (this.routes == null) {
            return NginxResponses.invalidUrl(url);
        }

        List<String> tokens = getRouteTokens(url);
        NginxRouteToken route = null;
        Map<String, NginxRouteToken> routes = this.routes;

        for (String token : tokens) {
            route = routes == null ? null : routes.getOrDefault(token, null);
            if (route == null) {
                return NginxResponses.invalidUrl(url);
            }
            routes = route.getChildren();
        }

        String method = (String) request.get(REQUEST_METHOD);
        if (!method.equals(route.getMethod())) {
            return NginxResponses.notSupportedMethod(url, method);
        }

        return route.getHandler().invoke(request);
    }

    public void get(String url, NginxRequestHandler handler) {
        addHandler(url, GET, handler);
        addHandler(url, POST, handler);
    }

    public void post(String url, NginxRequestHandler handler) {
        addHandler(url, POST, handler);
    }

    public void put(String url, NginxRequestHandler handler) {
        addHandler(url, PUT, handler);
    }

    public void delete(String url, NginxRequestHandler handler) {
        addHandler(url, DELETE, handler);
    }

    public void head(String url, NginxRequestHandler handler) {
        addHandler(url, HEAD, handler);
    }

    private void addHandler(String url, String method, NginxRequestHandler handler) {
        if (this.routes == null) {
            this.routes = new HashMap<>();
        }

        List<String> tokens = getRouteTokens(url);
        int size = tokens.size();
        int count = 0;
        Map<String, NginxRouteToken> routes = this.routes;

        for (String token : tokens) {
            count++;

            NginxRouteToken route = routes.getOrDefault(token, null);
            if (route == null) {
                route = new NginxRouteToken().setToken(token);

                if (count == size) {
                    route.setMethod(method).setHandler(handler);
                } else {
                    route.setChildren(new HashMap<>());
                }

                routes.put(token, route);

                routes = route.getChildren();
            }
        }
    }

    private List<String> getRouteTokens(String url) {
        List<String> list = new ArrayList<>();

        if (url != null) {
            String[] tokens = url.split("/");
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];
                if (!isEmptyOrNull(token)) {
                    list.add(token);
                }
            }
        }

        return list;
    }
}
