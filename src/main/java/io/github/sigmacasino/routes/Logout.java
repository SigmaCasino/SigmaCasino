package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.GetRoute;
import spark.Request;
import spark.Response;

/**
 * The Logout class handles the GET request for logging out the user.
 */
public class Logout extends GetRoute {
    /**
     * Constructs a Logout route with the specified application.
     *
     * @param app the application instance
     */
    public Logout(App app) {
        super(app, "/logout");
    }

    /**
     * Handles the GET request for logging out the user. This method invalidates the user's session and redirects them
     * to the home page.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return null
     */
    @Override
    public Object handleGet(Request request, Response response) {
        request.session().invalidate();
        response.redirect("/");
        return "Logged out successfully!";
    }
}
