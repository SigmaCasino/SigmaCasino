package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.GetRoute;
import spark.Request;
import spark.Response;

public class Logout extends GetRoute {

    public Logout(App app) {
        super(app, "/logout");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
    }
}
