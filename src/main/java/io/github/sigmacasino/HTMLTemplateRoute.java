package io.github.sigmacasino;

import java.util.Map;
import spark.Request;
import spark.Response;

public abstract class HTMLTemplateRoute extends GetRoute {
    protected HTMLTemplateRoute(App app, String path) {
        super(app, path);
    }

    @Override
    public Object handle(Request request, Response response) {
        var template = app.readResource("templates/" + getHTMLTemplatePath(request));
        response.type("text/html");
        return app.getJinjava().render(template, populateContext(request));
    }

    public abstract String getHTMLTemplatePath(Request request);

    public abstract Map<String, Object> populateContext(Request request);
}
