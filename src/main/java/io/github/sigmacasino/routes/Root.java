package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import java.util.Map;
import spark.Request;

/**
 * The root route of the application (empty path).
 * This route serves the index.html template.
 */
public class Root extends HTMLTemplateRoute {
    public Root(App app) {
        super(app, "/");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "index.html";
    }

    @Override
    public Map<String, Object> populateContext(Request request) {
        return Map.of();
    }
}
