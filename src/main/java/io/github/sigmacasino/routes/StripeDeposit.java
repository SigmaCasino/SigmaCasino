package io.github.sigmacasino.routes;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

public class StripeDeposit extends PostRoute {

    public StripeDeposit(App app) { super(app, "/stripe_checkout"); }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String YOUR_DOMAIN = "http://localhost:6969";
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(YOUR_DOMAIN + "/stripe_result?success=true")
                        .setCancelUrl(YOUR_DOMAIN + "/stripe_result?success=false")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(2L)
                                        // Provide the exact Price ID (for example, pr_1234) of the product you want to sell
                                        .setPrice("price_1QPM04J5OiGmTKU6RQZlrthB")
                                        .build())
                        .build();
        Session session = Session.create(params);

        response.redirect(session.getUrl(), 303);
        return "";
    }
}
