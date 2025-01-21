package io.github.sigmacasino.routes.docs;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the tests documentation page.
 */
public class Tests extends HTMLTemplateRoute {
    public Tests(App app) {
        super(app, "/docs/tests");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/tests.html";
    }
}
