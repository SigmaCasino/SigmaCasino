package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

import java.util.Map;

public class Login extends HTMLTemplateRoute {

    public Login(App app) { super(app, "/login"); }

    @Override
    public String getHTMLTemplatePath(Request request) { return "login.html"; }

    @Override
    public Map<String, Object> populateContext(Request request) { return Map.of(); }
}

