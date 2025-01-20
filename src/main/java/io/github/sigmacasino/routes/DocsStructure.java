package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the games page.
 */
public class DocsStructure extends HTMLTemplateRoute {
    public DocsStructure(App app) {
        super(app, "/docs/structure");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/structure.html";
    }
}
