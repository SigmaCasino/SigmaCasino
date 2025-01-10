package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

public class LoginPost extends PostRoute {

    public LoginPost(App app) {
        super(app, "/login");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null; // TODO ja
    }
}
