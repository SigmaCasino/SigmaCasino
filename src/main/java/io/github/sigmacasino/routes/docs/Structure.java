package io.github.sigmacasino.routes.docs;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the project structure page.
 */
public class Structure extends HTMLTemplateRoute {
    public Structure(App app) {
        super(app, "/docs/structure");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/structure.html";
    }
}
