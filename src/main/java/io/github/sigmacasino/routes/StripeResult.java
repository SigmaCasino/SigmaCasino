package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.GetRoute;
import spark.Request;
import spark.Response;

/**
 * Simple demo route that shows the result of the Stripe payment.
 * It displays an unformatted text message based on the payment status.
 */
public class StripeResult extends GetRoute {

    public StripeResult(App app) { super(app, "/stripe_result"); }

    /**
     * Displays a simple text message based on the payment status which is encoded in a query parameter named "success".
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return One of two text messages chosen by the "success" query parameter.
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        if (request.queryParams("success").equals("true")) {
            return "Płatność się powiodła";
        }
        else {
            return "Płatność została anulowana";
        }
    }
}
