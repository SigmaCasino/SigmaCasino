package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the games page.
 */
public class DocsUR extends HTMLTemplateRoute {
    public DocsUR(App app) {
        super(app, "/docs/ur");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/ur.html";
    }
}
