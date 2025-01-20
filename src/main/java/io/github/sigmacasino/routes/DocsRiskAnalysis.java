package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the games page.
 */
public class DocsRiskAnalysis extends HTMLTemplateRoute {
    public DocsRiskAnalysis(App app) {
        super(app, "/docs/docs_analysis");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/docs_analysis.html";
    }
}
