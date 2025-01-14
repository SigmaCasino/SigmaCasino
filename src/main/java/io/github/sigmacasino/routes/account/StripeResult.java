package io.github.sigmacasino.routes.account;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import java.util.Map;
import spark.Request;
import spark.Response;

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

    /**
     * Populates context with the payment success status.
     */
    @Override
    public Map<String, Object> populateContext(Request request, Response response) {
        return Map.of("payment_success", request.queryParams("payment_success"));
    }
}
