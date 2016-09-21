package productizer.nginx;

import nginx.clojure.java.ArrayMap;
import nginx.clojure.java.Constants;

import static nginx.clojure.MiniConstants.NGX_HTTP_BAD_REQUEST;

/**
 * Created by ymetelkin on 9/21/16.
 */
public class NginxUtils {
    private static final ArrayMap JSON_CONTENT_TYPE = ArrayMap.create(Constants.CONTENT_TYPE, "text/json");

    public static Object[] jsonResponse(String json) {
        return new Object[]{Constants.NGX_HTTP_OK, JSON_CONTENT_TYPE, json};
    }

    public static Object[] jsonResponse(String json, int httpStatus) {
        return new Object[]{httpStatus, JSON_CONTENT_TYPE, json};
    }

    public static Object[] invalidUrlResponse(String url){
        String error = String.format("{ \"error\": \"Invalid url: %s\" }", url);
        return jsonResponse(error, NGX_HTTP_BAD_REQUEST);
    }

    public static Object[] invalidUrlMethodResponse(String url, String method){
        String error = String.format("{ \"error\": \"Invalid method: %s %s\" }", method.toUpperCase(), url);
        return jsonResponse(error, NGX_HTTP_BAD_REQUEST);
    }
}
