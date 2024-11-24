package io.github.sigmacasino;

import spark.Spark;

public abstract class PostRoute extends HTTPRoute {
    protected PostRoute(App app, String path) {
        super(app, path);
    }

    @Override
    public final void registerSparkRoute() {
        Spark.post(path, this);
    }
}
