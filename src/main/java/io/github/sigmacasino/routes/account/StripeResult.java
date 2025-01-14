package io.github.sigmacasino.routes.account;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

/**
 * A route that shows the result of a Stripe payment to the user.
 */
public class StripeResult extends HTMLTemplateRoute {
    public StripeResult(App app) {
        super(app, "/account/stripe_result");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "account/stripe_result.html";
    }
}
