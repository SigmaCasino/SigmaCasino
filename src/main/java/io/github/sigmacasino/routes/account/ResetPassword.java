package io.github.sigmacasino.routes.account;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import java.util.Map;
import spark.Request;

/**
 * The ResetPassword class handles the GET request for displaying the reset password page.
 */
public class ResetPassword extends HTMLTemplateRoute {
    /**
     * A map of error messages that can be displayed on the reset password page.
     */
    public static final Map<String, String> ERRORS = Map.of(
        "invalid_user",
        "Invalid user credentials",
        "registered",
        "User registered successfully - now use your info to log in"
    );

    /**
     * Constructs a ResetPassword route with the specified application.
     *
     * @param app the application instance
     */
    public ResetPassword(App app) {
        super(app, "/account/reset_password");
    }

    /**
     * Returns the path to the HTML template for the reset password page.
     *
     * @param request the HTTP request
     * @return the path to the HTML template
     */
    @Override
    public String getHTMLTemplatePath(Request request) {
        return "account/reset_password.html";
    }

    /**
     * Populates the context for the reset password page.
     *
     * @param request the HTTP request
     * @return a map of context variables
     */
    @Override
    public Map<String, Object> populateContext(Request request) {
        return Map.of();
    }
}
