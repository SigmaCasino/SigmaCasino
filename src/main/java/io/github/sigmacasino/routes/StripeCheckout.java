package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import java.util.Map;
import spark.Request;

/**
 * A route that renders the demo Stripe Checkout page.
 * It allows user to enter the chosen amount of cash to deposit and be redirected to Stripe's website.
 */
public class StripeCheckout extends HTMLTemplateRoute {
    public StripeCheckout(App app) {
        super(app, "/stripe_checkout");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "stripe_checkout.html";
    }

    /**
     * Empty for now, in the future it will be used to pass user account data to the template.
     */
    @Override
    public Map<String, Object> populateContext(Request request) {
        return Map.of();
    }
}
