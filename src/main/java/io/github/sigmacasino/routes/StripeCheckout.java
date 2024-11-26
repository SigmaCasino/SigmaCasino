package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

import java.util.Map;

public class StripeCheckout extends HTMLTemplateRoute {

    public StripeCheckout(App app) { super(app, "/stripe_checkout"); }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "stripe_checkout.html";
    }

    @Override
    public Map<String, Object> populateContext(Request request) {
        return Map.of();
    }
}
