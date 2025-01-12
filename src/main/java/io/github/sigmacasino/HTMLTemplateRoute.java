package io.github.sigmacasino;

import java.util.Map;
import spark.Request;
import spark.Response;

/**
 * A GET route that renders a Jinjava HTML template.
 */
public abstract class HTMLTemplateRoute extends GetRoute {
    protected HTMLTemplateRoute(App app, String path) {
        super(app, path);
    }

    /**
     * Renders a Jinjava HTML template.
     */
    @Override
    public Object handle(Request request, Response response) {
        var template = app.readResource("templates/" + getHTMLTemplatePath(request));
        response.type("text/html");
        return app.getJinjava().render(template, populateContext(request));
    }

    /**
     * Returns the path to the Jinjava HTML template.
     */
    public abstract String getHTMLTemplatePath(Request request);

    /**
     * Returns the context to populate the Jinjava HTML template.
     * 
     * @param request the request
     * @return the context as a variable-value map
     */
    public abstract Map<String, Object> populateContext(Request request);
}
