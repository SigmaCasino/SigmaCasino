package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

import java.util.Map;

public class Register extends HTMLTemplateRoute {

    public Register(App app) { super(app, "/register"); }

    @Override
    public String getHTMLTemplatePath(Request request) { return "register.html"; }

    @Override
    public Map<String, Object> populateContext(Request request) { return Map.of(); }
}
