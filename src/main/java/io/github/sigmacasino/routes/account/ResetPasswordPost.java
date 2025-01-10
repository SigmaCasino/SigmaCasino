package io.github.sigmacasino.routes.account;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

public class ResetPasswordPost extends PostRoute {

    public ResetPasswordPost(App app) {
        super(app, "/account/reset_password");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null; // TODO
    }
}