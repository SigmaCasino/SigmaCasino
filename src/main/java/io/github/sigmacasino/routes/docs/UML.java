package io.github.sigmacasino.routes.docs;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the UML diagrams page.
 */
public class UML extends HTMLTemplateRoute {
    public UML(App app) {
        super(app, "/docs/uml");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/uml.html";
    }
}
