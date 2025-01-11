package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import java.util.Map;
import spark.Request;

public class Account extends HTMLTemplateRoute {
    public Account(App app) {
        super(app, "/account");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "account.html";
    }

    @Override
    public Map<String, Object> populateContext(Request request) {
        return Map.of(); // TODO pobierać poprzednie gry i transakcje z DB
    }
}
