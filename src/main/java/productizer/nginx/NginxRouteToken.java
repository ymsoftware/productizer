package productizer.nginx;

import nginx.clojure.java.NginxJavaRingHandler;

import java.util.Map;

/**
 * Created by ymetelkin on 9/22/16.
 */
public class NginxRouteToken {
    private String token;
    private String method;
    private NginxRequestHandler handler;
    private Map<String, NginxRouteToken> children;
    private String arg;

    public String getToken() {
        return this.token;
    }

    public NginxRouteToken setToken(String token) {
        this.token = token;
        return this;
    }

    public String getMethod() {
        return this.method;
    }

    public NginxRouteToken setMethod(String method) {
        this.method = method;
        return this;
    }

    public NginxRequestHandler getHandler() {
        return this.handler;
    }

    public NginxRouteToken setHandler(NginxRequestHandler handler) {
        this.handler = handler;
        return this;
    }

    public Map<String, NginxRouteToken> getChildren() {
        return this.children;
    }

    public NginxRouteToken setChildren(Map<String, NginxRouteToken> children) {
        this.children = children;
        return this;
    }

    public String getArg() {
        return this.arg;
    }

    public NginxRouteToken setArg(String arg) {
        if (this.arg != null && this.arg != arg) {
            throw new IllegalArgumentException(String.format("There is %s parameter already!"));
        }
        this.arg = arg;
        return this;
    }
}
