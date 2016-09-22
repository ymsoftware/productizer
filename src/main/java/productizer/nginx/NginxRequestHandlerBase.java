package productizer.nginx;

import java.util.Map;

/**
 * Created by ymetelkin on 9/22/16.
 */
public abstract class NginxRequestHandlerBase implements NginxRequestHandler {
    abstract Object[] execute(Map<String, Object> request);

    @Override
    public Object[] invoke(Map<String, Object> request) {
        try{
            Object[] response=execute(request);
            return response;
        }catch (Exception ex){
            return NginxResponses.exception(ex);
        }
    }
}
