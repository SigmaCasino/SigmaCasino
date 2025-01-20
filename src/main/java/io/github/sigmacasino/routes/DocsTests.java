package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the games page.
 */
public class DocsTests extends HTMLTemplateRoute {
    public DocsTests(App app) {
        super(app, "/docs/tests");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/tests.html";
    }
}
