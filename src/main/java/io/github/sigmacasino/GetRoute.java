package io.github.sigmacasino;

import java.sql.SQLException;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * A route that handles GET requests.
 */
public abstract class GetRoute extends HTTPRoute {
    /**
     * Creates a new GET route.
     *
     * @param app app instance, passed to the super contructor
     * @param path the path of the route, passed to the super contructor
     */
    protected GetRoute(App app, String path) {
        super(app, path);
    }

    /**
     * Handles a GET request by running super.handle() and then calling handleGet().
     *
     * @param request the request
     * @param response the response
     * @return the response body
     */
    @Override
    public final Object handle(Request request, Response response) throws Exception {
        if ((boolean) super.handle(request, response)) {
            return "Redirecting to login...";
        }
        return handleGet(request, response);
    }

    @Override
    public final void registerSparkRoute() {
        Spark.get(path, this);
    }

    /**
     * Handles a GET request.
     *
     * @param request the request
     * @param response the response
     * @return the response body
     * @throws SQLException if a database access error occurs in any subclass implementation
     */
    public abstract Object handleGet(Request request, Response response) throws SQLException;
}
