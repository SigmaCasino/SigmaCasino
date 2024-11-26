package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.GetRoute;
import spark.Request;
import spark.Response;

public class StripeResult extends GetRoute {

    public StripeResult(App app) { super(app, "/stripe_result"); }

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
