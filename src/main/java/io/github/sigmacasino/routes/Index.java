package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.GetRoute;
import spark.Request;
import spark.Response;

/**
 * Redirects the user to the main page.
 */
public class Index extends GetRoute {
    public Index(App app) {
        super(app, "/index");
    }

    /**
     * Redirects the user to the main page.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return A message indicating that the user is being redirected.
     */
    @Override
    public Object handleGet(Request request, Response response) {
        response.redirect("/", 301);
        return "Redirecting to the main page...";
    }
}
