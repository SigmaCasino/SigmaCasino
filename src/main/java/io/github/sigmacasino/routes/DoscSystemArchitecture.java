package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the games page.
 */
public class DoscSystemArchitecture extends HTMLTemplateRoute {
    public DoscSystemArchitecture(App app) {
        super(app, "/docs/system_architecture");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/system_architecture.html";
    }
}
