package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the games page.
 */
public class DocsRequirements extends HTMLTemplateRoute {
    public DocsRequirements(App app) {
        super(app, "/docs/requirements");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/requirements.html";
    }
}
