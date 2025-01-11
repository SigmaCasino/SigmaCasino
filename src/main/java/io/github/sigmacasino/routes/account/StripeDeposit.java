package io.github.sigmacasino.routes.account;

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
     * @return An empty page with a Stripe redirect.
     * @see SessionCreateParams
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        SessionCreateParams params =
            SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(app.getDomain() + "/account/stripe_result?success=true")
                .setCancelUrl(app.getDomain() + "/account/stripe_result?success=false")
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(2L)
                        // Provide the exact Price ID (for example, pr_1234) of the product you want to sell
                        .setPrice("price_1QPM04J5OiGmTKU6RQZlrthB")
                        .build()
                )
                .build();
        Session session = Session.create(params);

        response.redirect(session.getUrl(), 303);
        return "";
    }
}
