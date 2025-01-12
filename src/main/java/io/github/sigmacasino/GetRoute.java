package io.github.sigmacasino;

import spark.Spark;

/**
 * A route that handles GET requests.
 */
public abstract class GetRoute extends HTTPRoute {
    protected GetRoute(App app, String path) {
        super(app, path);
    }

    @Override
    public final void registerSparkRoute() {
        Spark.get(path, this);
    }
}
