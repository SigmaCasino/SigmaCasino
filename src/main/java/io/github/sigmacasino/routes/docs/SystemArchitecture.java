package io.github.sigmacasino.routes.docs;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the games page.
 */
public class SystemArchitecture extends HTMLTemplateRoute {
    public SystemArchitecture(App app) {
        super(app, "/docs/system_architecture");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/system_architecture.html";
    }
}
