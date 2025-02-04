package io.github.sigmacasino;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * A route that handles POST requests.
 */
public abstract class PostRoute extends HTTPRoute {
    /**
     * The logger for the PostRoute class.
     */
    private static Logger postRouteLogger = LoggerFactory.getLogger(PostRoute.class);

    /**
     * Constructs a new PostRoute.
     * @param app app instance, passed to the super contructor
     * @param path the path of the route, passed to the super contructor
     */
    protected PostRoute(App app, String path) {
        super(app, path);
    }

    /**
     * Runs super.handle() and then calls handlePost().
     * @return empty string
     */
    @Override
    public final Object handle(Request request, Response response) throws Exception {
        if ((boolean) super.handle(request, response)) {
            return "Redirecting to login...";
        }
        handlePost(request, response);
        return "";
    }

    @Override
    public final void registerSparkRoute() {
        Spark.post(path, this);
    }

    /**
     * Handles a POST request.
     *
     * @param request the request
     * @param response the response
     */
    public abstract void handlePost(Request request, Response response) throws SQLException;

    /**
     * Parses the body of a POST request into a map of key-value pairs.
     * The body must be encoded in the application/x-www-form-urlencoded format.
     *
     * @param request the request to parse
     * @return a map of key-value pairs
     */
    public static final Map<String, String> parseBodyParams(Request request) {
        var body = request.body();
        Map<String, String> params = new HashMap<>();
        for (var pair : body.split("&")) {
            var keyValue = pair.split("=");
            try {
                params.put(URLDecoder.decode(keyValue[0], "UTF-8"), URLDecoder.decode(keyValue[1], "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                postRouteLogger.error("Failed to decode URL-encoded body parameter", e);
            }
        }
        return params;
    }
}
