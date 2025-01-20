package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the games page.
 */
public class DocsTechStack extends HTMLTemplateRoute {
    public DocsTechStack(App app) {
        super(app, "/docs/tech_stack");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/tech_stack.html";
    }
}
