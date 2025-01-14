package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import java.util.Map;
import spark.Request;

/**
 * The Login class handles the GET request for displaying the login page.
 */
public class Login extends HTMLTemplateRoute {
    /**
     * Constructs a Login route with the specified application.
     *
     * @param app the application instance
     */
    public Login(App app) {
        super(app, "/login");
    }

    /**
     * Returns the path to the HTML template for the login page.
     *
     * @param request the HTTP request
     * @return the path to the HTML template
     */
    @Override
    public String getHTMLTemplatePath(Request request) {
        return "login.html";
    }

    @Override
    public Map<String, String> getNotificationDefinitions() {
        return Map.of(
            "invalid_user",
            "Invalid user credentials",
            "registered",
            "User registered successfully - now use your info to log in",
            "must_be_logged_in",
            "You must be logged in to access that page"
        );
    }
}
