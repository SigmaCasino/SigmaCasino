package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import java.util.Map;
import spark.Request;

/**
 * The Register class handles the GET request for displaying the registration page.
 */
public class Register extends HTMLTemplateRoute {
    /**
     * A map of error messages that can be displayed on the registration page.
     * The keys are error codes and the values are the corresponding error messages.
     */
    public static final Map<String, String> ERRORS = Map.of(
        "username_length", "Username must be between 3 and 16 characters",
        "email_invalid", "Invalid email address",
        "password_length", "Password must be at least 8 characters",
        "user_exists", "User with that username or email already exists"
    );

    /**
     * Constructs a Register route with the specified application.
     *
     * @param app the application instance
     */
    public Register(App app) {
        super(app, "/register");
    }

    /**
     * Returns the path to the HTML template for the registration page.
     *
     * @param request the HTTP request
     * @return the path to the HTML template
     */
    @Override
    public String getHTMLTemplatePath(Request request) {
        return "register.html";
    }

    /**
     * Populates the context for the registration page.
     *
     * @param request the HTTP request
     * @return a map of context variables
     */
    @Override
    public Map<String, Object> populateContext(Request request) {
        return Map.of();
    }
}
