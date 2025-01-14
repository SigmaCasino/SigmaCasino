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
     * A map of error messages that can be displayed on the login page.
     */
    public static final Map<String, String> ERRORS = Map.of(
        "invalid_user",
        "Invalid user credentials",
        "registered",
        "User registered successfully - now use your info to log in"
    );

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

    /**
     * Populates the context for the login page.
     *
     * @param request the HTTP request
     * @return a map of context variables
     */
    @Override
    public Map<String, Object> populateContext(Request request) {
        return Map.of();
    }
}
