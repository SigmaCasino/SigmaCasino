package io.github.sigmacasino.routes.docs;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the interfaces documentation page.
 */
public class Interfaces extends HTMLTemplateRoute {
    public Interfaces(App app) {
        super(app, "/docs/interfaces");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/interfaces.html";
    }
}
