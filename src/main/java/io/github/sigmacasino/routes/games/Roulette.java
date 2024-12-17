package io.github.sigmacasino.routes.games;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

import java.util.Map;

public class Roulette extends HTMLTemplateRoute {

    public Roulette(App app) {
        super(app, "/games/roulette");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "games/roulette.html";
    }

    @Override
    public Map<String, Object> populateContext(Request request) {
        return Map.of();
    }
}
