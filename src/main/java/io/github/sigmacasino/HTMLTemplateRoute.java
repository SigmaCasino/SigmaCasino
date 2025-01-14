package io.github.sigmacasino;

import java.util.HashMap;
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
     * Also, populates its context with the request attributes and the result of the
     * {@link #populateContext(Request)} method.
     */
    @Override
    public Object handle(Request request, Response response) {
        var template = app.readResource("templates/" + getHTMLTemplatePath(request));
        response.type("text/html");
        var context = new HashMap<String, Object>();
        request.attributes().stream().forEach(k -> context.put(k, request.attribute(k)));
        context.putAll(populateContext(request));
        return app.getJinjava().render(template, context);
    }

    /**
     * Returns the path to the Jinjava HTML template.
     */
    public abstract String getHTMLTemplatePath(Request request);

    /**
     * Returns additional context to populate the Jinjava HTML template with.
     * By default, it returns an empty map.
     *
     * @param request the request
     * @return the context as a variable-value map
     */
    public Map<String, Object> populateContext(Request request) {
        return Map.of();
    }
}
