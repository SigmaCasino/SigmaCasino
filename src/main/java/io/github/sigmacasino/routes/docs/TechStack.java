package io.github.sigmacasino.routes.docs;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the tech stack documentation page.
 */
public class TechStack extends HTMLTemplateRoute {
    public TechStack(App app) {
        super(app, "/docs/tech_stack");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/tech_stack.html";
    }
}
