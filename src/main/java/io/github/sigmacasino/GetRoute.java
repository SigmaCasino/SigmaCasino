package io.github.sigmacasino;

import java.sql.SQLException;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * A route that handles GET requests.
 */
public abstract class GetRoute extends HTTPRoute {
    protected GetRoute(App app, String path) {
        super(app, path);
    }

    @Override
    public final Object handle(Request request, Response response) throws Exception {
        super.handle(request, response);
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
     */
    public abstract Object handleGet(Request request, Response response) throws SQLException;
}
