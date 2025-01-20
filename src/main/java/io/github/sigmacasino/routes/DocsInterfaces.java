package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the games page.
 */
public class DocsInterfaces extends HTMLTemplateRoute {
    public DocsInterfaces(App app) {
        super(app, "/docs/interfaces");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/interfaces.html";
    }
}
