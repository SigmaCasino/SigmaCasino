package io.github.sigmacasino.routes.account;

import java.sql.SQLException;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

/**
 * A post route which initiates a Stripe payment session.
 * It redirects the user to the Stripe Checkout page via the official API.
 * @see <a href="https://stripe.com/docs/api/checkout/sessions/create">Stripe API Reference</a>
 */
public class StripeDeposit extends PostRoute {
    public StripeDeposit(App app) {
        super(app, "/account/deposit");
    }

    /**
     * Initiates a Stripe purchase session and redirects the user to the Stripe Checkout page.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @see SessionCreateParams
     */
    @Override
    public void handlePost(Request request, Response response) throws SQLException {
        SessionCreateParams params =
            SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(app.getDomain() + "/account/stripe_result?payment_success=true")
                .setCancelUrl(app.getDomain() + "/account/stripe_result?payment_success=false")
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(2L)
                        // Provide the exact Price ID (for example, pr_1234) of the product you want to sell
                        .setPrice("price_1QPM04J5OiGmTKU6RQZlrthB")
                        .build()
                )
                .build();
        try {
            Session session = Session.create(params);
            response.redirect(session.getUrl());
        } catch (StripeException e) {
            response.redirect("/account/stripe_result?payment_success=false");
        }

    }
}
