package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the games page.
 */
public class Docs extends HTMLTemplateRoute {
    public Docs(App app) {
        super(app, "/docs");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs.html";
    }
}
