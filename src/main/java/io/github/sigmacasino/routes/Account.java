package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

import java.util.Map;

public class Account extends HTMLTemplateRoute {

    public Account(App app) { super(app, "/account"); }

    @Override
    public String getHTMLTemplatePath(Request request) { return "account.html"; }

    @Override
    public Map<String, Object> populateContext(Request request) { return Map.of(); }
}
