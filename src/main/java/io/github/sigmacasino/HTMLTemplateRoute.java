package io.github.sigmacasino;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;

/**
 * A GET route that renders a Jinjava HTML template.
 * @see com.hubspot.jinjava.Jinjava
 */
public abstract class HTMLTemplateRoute extends GetRoute {
    /**
     * A map of notification definitions to be used in the Jinjava HTML template.
     * The keys are the URL names of the notifications, and the values are the notification messages.
     */
    private final Map<String, String> notificationDefinitions;

    /**
     * Creates a new HTMLTemplateRoute.
     * @param app app instance, passed to the super contructor
     * @param path the path of the route, passed to the super contructor
     */
    protected HTMLTemplateRoute(App app, String path) {
        super(app, path);
        this.notificationDefinitions = getNotificationDefinitions();
    }

    /**
     * Renders a Jinjava HTML template.
     * It populates the context with the request session attributes and the result of the
     * {@link #populateContext(Request, Response)} method. It also adds a notification and its type to the context
     * if the request contains an "error" or "success" query parameter.
     *
     * @param request the request
     * @param response the response
     * @return the rendered HTML template as a string
     */
    @Override
    public Object handleGet(Request request, Response response) throws SQLException {
        var template = app.readResource("templates/" + getHTMLTemplatePath(request));
        response.type("text/html");
        var context = new HashMap<String, Object>();
        request.session().attributes().stream().forEach(k -> context.put(k, request.session().attribute(k)));
        context.putAll(populateContext(request, response));

        var error = request.queryParams("error");
        var success = request.queryParams("success");
        if (error != null && notificationDefinitions.containsKey(error)) {
            context.put("notification_kind", "error");
            context.put("notification", notificationDefinitions.get(error));
        } else if (success != null && notificationDefinitions.containsKey(success)) {
            context.put("notification_kind", "success");
            context.put("notification", notificationDefinitions.get(success));
        }

        return app.getJinjava().render(template, context);
    }

    /**
     * Returns the path to the Jinjava HTML template.
     */
    public abstract String getHTMLTemplatePath(Request request);

    /**
     * Returns a map of notification definitions to be used in the Jinjava HTML template.
     * Intended to be overridden by subclasses, providing their own definitions based on all
     * redirects which point the user to the route with any error or success tags.
     * @return the definitions as an URL name-notification message map.
     */
    public Map<String, String> getNotificationDefinitions() {
        return Map.of();
    }

    /**
     * Returns additional context to populate the Jinjava HTML template with.
     * By default, it returns an empty map.
     * Intended to be overriden by subclasses.
     *
     * @param request the request
     * @param response the response
     * @return the context as a variable-value map
     * @throws SQLException if a database access error occurs in any subclass implementation
     */
    public Map<String, Object> populateContext(Request request, Response response) throws SQLException {
        return Map.of();
    }
}
