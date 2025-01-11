package io.github.sigmacasino;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Spark;

public abstract class PostRoute extends HTTPRoute {
    private static Logger logger = LoggerFactory.getLogger(PostRoute.class);

    protected PostRoute(App app, String path) {
        super(app, path);
    }

    @Override
    public final void registerSparkRoute() {
        Spark.post(path, this);
    }

    public static final Map<String, String> parseBodyParams(Request request) {
        var body = request.body();
        Map<String, String> params = new HashMap<>();
        for (var pair : body.split("&")) {
            var keyValue = pair.split("=");
            try {
                params.put(URLDecoder.decode(keyValue[0], "UTF-8"), URLDecoder.decode(keyValue[1], "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                logger.error("Failed to decode URL-encoded body parameter", e);
            }
        }
        return params;
    }
}
