package productizer.nginx;

import nginx.clojure.java.ArrayMap;
import nginx.clojure.java.Constants;

import static nginx.clojure.MiniConstants.*;

/**
 * Created by ymetelkin on 9/22/16.
 */
public class NginxResponses {
    private static final ArrayMap JSON_CONTENT_TYPE = ArrayMap.create(Constants.CONTENT_TYPE, "text/json");

    public static Object[] json(String json) {
        return new Object[]{NGX_HTTP_OK, JSON_CONTENT_TYPE, json};
    }

    public static Object[] json(String json, int httpStatus) {
        return new Object[]{httpStatus, JSON_CONTENT_TYPE, json};
    }

    public static Object[] invalidUrl(String url){
        String error = String.format("{ \"error\": \"Invalid url: %s\" }", url);
        return json(error, NGX_HTTP_NOT_FOUND);
    }

    public static Object[] notSupportedMethod(String url, String method){
        String error = String.format("{ \"error\": \"%s is not supported: %s\" }", method.toUpperCase(), url);
        return json(error, NGX_HTTP_NOT_ALLOWED);
    }

    public static Object[] exception(Exception ex){
        String error = String.format("{ \"error\": \"%s\" }", ex.getMessage());
        return json(error, NGX_HTTP_BAD_REQUEST);
    }
}
