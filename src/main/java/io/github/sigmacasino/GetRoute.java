package io.github.sigmacasino;

import spark.Spark;

public abstract class GetRoute extends HTTPRoute {
    protected GetRoute(App app, String path) {
        super(app, path);
    }

    @Override
    public final void registerSparkRoute() {
        Spark.get(path, this);
    }
}
