package productizer.nginx;

import nginx.clojure.java.NginxJavaRingHandler;
import org.elasticsearch.common.Strings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nginx.clojure.MiniConstants.*;

/**
 * Created by ymetelkin on 9/21/16.
 */
public abstract class NginxRoutingHandlerBase implements NginxJavaRingHandler {
    private final NginxRouteToken root;
    private final List<String> prefix;

    public static final String REQUEST_ARGS = "request_args";

    public NginxRoutingHandlerBase() {
        this.root = new NginxRouteToken().setChildren(new HashMap<>());
        this.prefix = new ArrayList<>();
    }

    public NginxRoutingHandlerBase(String baseUrl) {
        this.root = new NginxRouteToken().setChildren(new HashMap<>());

        List<String> prefix = new ArrayList<>();

        List<String> tokens = getRouteTokens(baseUrl, false);
        if (tokens.size() > 0) {
            Map<String, NginxRouteToken> routes = this.root.getChildren();

            for (String token : tokens) {
                prefix.add(token);
                NginxRouteToken route = new NginxRouteToken().setToken(token).setChildren(new HashMap<>());
                routes.put(token, route);
                routes = route.getChildren();
            }
        }

        this.prefix = prefix;
    }

    @Override
    public Object[] invoke(Map<String, Object> request) throws IOException {
        String url = (String) request.get(URI);

        List<String> tokens = getRouteTokens(url, false);
        NginxRouteToken route = this.root;

        for (String token : tokens) {
            route = getToken(token, route, request);
            if (route == null) {
                return NginxResponses.invalidUrl(url);
            }
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
        List<String> tokens = getRouteTokens(url, true);
        int size = tokens.size();
        int count = 0;
        NginxRouteToken parent = this.root;

        for (String token : tokens) {
            count++;

            Map<String, NginxRouteToken> routes = parent.getChildren();
            if (routes == null) {
                parent.setChildren(new HashMap<>());
                routes = parent.getChildren();
            }

            NginxRouteToken route = routes.getOrDefault(token, null);
            if (route == null) {
                route = new NginxRouteToken().setToken(token);

                if (token.startsWith("{") && token.endsWith("}")) {
                    parent.setArg(token.substring(1, token.length() - 1));
                }

                if (count == size) {
                    route.setMethod(method).setHandler(handler);
                } else {
                    route.setChildren(new HashMap<>());
                }

                routes.put(token, route);
            }

            parent = route;
        }
    }

    private List<String> getRouteTokens(String url, boolean usePrefix) {
        List<String> list = new ArrayList<>();
        if (usePrefix) {
            list.addAll(this.prefix);
        }

        if (url != null) {
            String[] tokens = url.split("/");
            for (int i = 0; i < tokens.length; i++) {
                String token = tokens[i];
                if (!Strings.isNullOrEmpty(token)) {
                    list.add(token);
                }
            }
        }

        return list;
    }

    private NginxRouteToken getToken(String token, NginxRouteToken parent, Map<String, Object> request) {
        if (parent == null) return null;

        Map<String, NginxRouteToken> routes = parent.getChildren();
        if (routes == null) return null;

        NginxRouteToken route = routes.getOrDefault(token, null);
        if (route == null) {
            String arg = parent.getArg();
            if (!Strings.isNullOrEmpty(arg)) {
                String key = String.format("{%s}", arg);

                route = routes.getOrDefault(key, null);
                if (route != null) {
                    Map<String, Object> args = (Map<String, Object>) request.getOrDefault(REQUEST_ARGS, null);
                    if (args == null) args = new HashMap<>();
                    args.put(arg, token);
                    request.put(REQUEST_ARGS, args);
                }
            }
        }

        return route;
    }
}
