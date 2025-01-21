package io.github.sigmacasino.routes.account;

import io.github.sigmacasino.App;
import io.github.sigmacasino.GetRoute;
import java.sql.SQLException;
import spark.Request;
import spark.Response;

/**
 * A route that shows the result of a Stripe payment to the user.
 */
public class StripeResult extends GetRoute {
    public StripeResult(App app) {
        super(app, "/account/stripe_result");
    }

    /**
     * Redirects the user to the account page with a success or error message.
     * Temporary solution until we implement a proper payment result template.
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @return A redirect message.
     */
    @Override
    public Object handleGet(Request request, Response response) throws SQLException {
        var success = request.queryParams("payment_success").toLowerCase().startsWith("t");
        if (success) {
            response.redirect("/account?success=payment");
        } else {
            response.redirect("/account?error=payment_failed");
        }
        return "Redirecting...";
    }

    // @Override
    // public String getHTMLTemplatePath(Request request) {
    //     return "account/stripe_result.html";
    // }

    // /**
    //  * Populates context with the payment success status.
    //  */
    // @Override
    // public Map<String, Object> populateContext(Request request, Response response) {
    //     return Map.of("payment_success", request.queryParams("payment_success"));
    // }
}
