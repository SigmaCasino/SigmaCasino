package io.github.sigmacasino.routes.games;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

import java.util.Map;

public class Horses extends HTMLTemplateRoute {
    public Horses(App app) {
        super(app, "/games/horses");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "games/horses.html";
    }

    @Override
    public Map<String, Object> populateContext(Request request) {
        return Map.of();
    }
}
