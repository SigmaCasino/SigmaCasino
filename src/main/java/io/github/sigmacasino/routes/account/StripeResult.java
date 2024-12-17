package io.github.sigmacasino.routes.account;

import io.github.sigmacasino.App;
import io.github.sigmacasino.GetRoute;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;
import spark.Response;

import java.util.Map;

/**
 * Simple demo route that shows the result of the Stripe payment.
 * It displays an unformatted text message based on the payment status.
 */
public class StripeResult extends HTMLTemplateRoute {

    public StripeResult(App app) { super(app, "/account/stripe_result"); }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "account/stripe_result.html";
    }

    @Override
    public Map<String, Object> populateContext(Request request) {
        return Map.of();
    }
}
