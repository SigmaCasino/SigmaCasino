package io.github.sigmacasino;

/**
 * HTTPRoute is an abstract class that represents a route in the HTTP server.
 * It is a wrapper around the Spark Route interface, and is used to provide a
 * common fields for all routes in the server.
 */
public abstract class HTTPRoute implements spark.Route {
    /**
     * The App object that the route is associated with.
     */
    protected final App app;
    /**
     * The path of the route, e.g. "/account/reset_password".
     * This is the path that the route will be registered to with Spark.
     */
    protected final String path;

    /**
     * Constructor for the HTTPRoute class.
     * @param app The App object that the route is associated with.
     * @param path The path of the route.
     */
    protected HTTPRoute(App app, String path) {
        this.app = app;
        this.path = path;
    }

    /**
     * Registers the route with Spark.
     */
    public abstract void registerSparkRoute();
}
