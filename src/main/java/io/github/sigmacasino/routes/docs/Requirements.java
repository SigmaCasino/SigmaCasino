package io.github.sigmacasino.routes.docs;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the requirements page.
 */
public class Requirements extends HTMLTemplateRoute {
    public Requirements(App app) {
        super(app, "/docs/requirements");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/requirements.html";
    }
}
