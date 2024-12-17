package io.github.sigmacasino.routes.games;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

public class HorsesPost extends PostRoute {

    public HorsesPost(App app) {
        super(app, "/games/horses");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
    }
}
