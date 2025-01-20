package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the games page.
 */
public class DocsUML extends HTMLTemplateRoute {
    public DocsUML(App app) {
        super(app, "/docs/uml");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/uml.html";
    }
}
