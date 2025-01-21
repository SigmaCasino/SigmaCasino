package io.github.sigmacasino;

import spark.Request;
import spark.Response;

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
     * Whether the route requires the user to be logged in.
     */
    protected boolean loginRequired = false;

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
     * Handles the request and returns the response.
     * This method is called by Spark when a request is made to the route.
     * It should be overridden by subclasses to provide the route's functionality.
     * Additionally, this super-implementation handles redirecting the user to the login page if the route
     * requires the user to be logged in and returns boolean which says whether the redirect should occur.
     *
     * @param request The request object.
     * @param response The response object.
     * @return The response object, which might be text, HTTP code, etc.
     * @throws Exception If an error occurs while handling the request.
     * @see spark.Route#handle(Request, Response)
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        if (loginRequired && request.session().attribute("user_id") == null) {
            response.redirect("/login?error=must_be_logged_in");
            return true;
        }
        return false;
    }

    /**
     * Registers the route with Spark.
     */
    public abstract void registerSparkRoute();
}
