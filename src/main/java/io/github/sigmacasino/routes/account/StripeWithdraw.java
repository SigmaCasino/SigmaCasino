package io.github.sigmacasino.routes.account;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

/**
 * A post route which initiates a Stripe payment session.
 * It redirects the user to the Stripe Checkout page via the official API.
 * @see <a href="https://stripe.com/docs/api/checkout/sessions/create">Stripe API Reference</a>
 */
public class StripeWithdraw extends PostRoute {
    public StripeWithdraw(App app) {
        super(app, "/account/withdraw");
    }

    /**
     * Initiates a Stripe purchase session and redirects the user to the Stripe Checkout page.
     * @param request The HTTP request.
     * @param response The HTTP response.
     */
    @Override
    public void handlePost(Request request, Response response) {
        // TODO implement: https://docs.stripe.com/connect/add-and-pay-out-guide?lang=java
        response.redirect("/account/stripe_result?payment_success=false");
    }
}
