package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import java.util.Map;
import spark.Request;

/**
 * The route for the games page.
 */
public class Games extends HTMLTemplateRoute {
    public Games(App app) {
        super(app, "/games");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "games.html";
    }

    @Override
    public Map<String, Object> populateContext(Request request) {
        return Map.of();
    }
}
