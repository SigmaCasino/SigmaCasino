package io.github.sigmacasino.routes.games;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

public class RoulettePost extends PostRoute {

    public RoulettePost(App app) {
        super(app, "/games/roulette");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;  // TODO zarejestrować nową grę do DB i przekierować użytkownika na replay
    }
}
