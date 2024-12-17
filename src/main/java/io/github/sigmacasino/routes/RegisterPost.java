package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

public class RegisterPost extends PostRoute {

    public RegisterPost(App app) {
        super(app, "/register");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
    }
}
