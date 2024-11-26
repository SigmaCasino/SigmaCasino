package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.GetRoute;
import spark.Request;
import spark.Response;

public class Index extends GetRoute {
    public Index(App app) {
        super(app, "/index");
    }

    @Override
    public Object handle(Request request, Response response) {
        response.redirect("/", 301);
        return "Redirecting to the main page...";
    }
}
