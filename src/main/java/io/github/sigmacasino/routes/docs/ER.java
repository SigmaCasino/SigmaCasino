package io.github.sigmacasino.routes.docs;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the ER diagrams page.
 */
public class ER extends HTMLTemplateRoute {
    public ER(App app) {
        super(app, "/docs/ur");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/ur.html";
    }
}
