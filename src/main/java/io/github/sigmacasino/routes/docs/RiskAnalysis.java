package io.github.sigmacasino.routes.docs;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * The route for the risk analysis documentation page.
 */
public class RiskAnalysis extends HTMLTemplateRoute {
    public RiskAnalysis(App app) {
        super(app, "/docs/docs_analysis");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/docs_analysis.html";
    }
}
