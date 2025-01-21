package io.github.sigmacasino.routes.account;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import java.sql.SQLException;
import spark.Request;
import spark.Response;

/**
 * A post route which initiates a Stripe payment session.
 * It redirects the user to the Stripe Checkout page via the official API.
 * @see <a href="https://stripe.com/docs/api/checkout/sessions/create">Stripe API Reference</a>
 */
public class StripeDeposit extends PostRoute {
    private String priceIdentifier = System.getenv("STRIPE_PRICE_ID");

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
        var bodyParams = parseBodyParams(request);
        long amount;
        try {
            amount = Long.parseLong(bodyParams.get("amount"));
        } catch (NumberFormatException e) {
            response.redirect("/account/stripe_result?payment_success=false");
            return;
        }

        if (amount <= 0) {
            response.redirect("/account/stripe_result?payment_success=false");
            return;
        }

        SessionCreateParams params =
            SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(app.getDomain() + "/account/stripe_result?payment_success=true")
                .setCancelUrl(app.getDomain() + "/account/stripe_result?payment_success=false")
                .addLineItem(
                    SessionCreateParams.LineItem.builder().setQuantity(amount).setPrice(priceIdentifier).build()
                )
                .putMetadata("user_id", request.session().attribute("user_id").toString())
                .build();

        try {
            Session session = Session.create(params);
            response.redirect(session.getUrl());
        } catch (StripeException e) {
            response.redirect("/account/stripe_result?payment_success=false");
        }
    }
}
