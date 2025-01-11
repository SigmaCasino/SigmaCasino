package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import java.util.Map;
import spark.Request;

public class Register extends HTMLTemplateRoute {
    public static final Map<String, String> ERRORS = Map.of(
        "username_length", "Username must be between 3 and 16 characters",
        "email_invalid", "Invalid email address",
        "password_length", "Password must be at least 8 characters",
        "user_exists", "User with that username or email already exists"
    );
    
    public Register(App app) {
        super(app, "/register");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "register.html";
    }

    @Override
    public Map<String, Object> populateContext(Request request) {
        return Map.of();
    }
}
