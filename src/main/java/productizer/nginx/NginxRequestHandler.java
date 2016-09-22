package productizer.nginx;

import java.util.Map;

/**
 * Created by ymetelkin on 9/22/16.
 */
public interface NginxRequestHandler {
    Object[] invoke(Map<String, Object> request);
}