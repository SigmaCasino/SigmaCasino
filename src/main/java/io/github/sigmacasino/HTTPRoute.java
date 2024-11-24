package io.github.sigmacasino;

public abstract class HTTPRoute implements spark.Route {
    protected final App app;
    protected final String path;

    protected HTTPRoute(App app, String path) {
        this.app = app;
        this.path = path;
    }

    public abstract void registerSparkRoute();
}
