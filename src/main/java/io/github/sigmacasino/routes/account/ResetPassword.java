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
     * Constructs a ResetPassword route with the specified application.
     *
     * @param app the application instance
     */
    public ResetPassword(App app) {
        super(app, "/account/reset_password");
        this.loginRequired = true;
    }

    @Override
    public Map<String, String> getNotificationDefinitions() {
        return Map.of(
            "invalid_user",
            "Invalid user credentials",
            "registered",
            "User registered successfully - now use your info to log in"
        );
    }

    /**
     * Returns the path to the HTML template for the reset password page.
     *
     * @param request the HTTP request
     * @return the path to the HTML template
     */
    @Override
    public String getHTMLTemplatePath(Request request) {
        return "reset_password.html";
    }
}
