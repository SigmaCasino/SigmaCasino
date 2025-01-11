package io.github.sigmacasino.routes.account;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

import java.util.Map;

public class ResetPassword extends HTMLTemplateRoute {
    public static final Map<String, String> ERRORS = Map.of(
            "invalid_user", "Invalid user credentials",
            "registered", "User registered successfully - now use your info to log in"
    );

    public ResetPassword(App app) {
        super(app, "/account/reset_password");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "account/reset_password.html";
    }

    @Override
    public Map<String, Object> populateContext(Request request) {
        return Map.of();
    }
}
