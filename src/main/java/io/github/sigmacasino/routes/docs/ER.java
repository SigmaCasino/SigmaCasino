package io.github.sigmacasino.routes.docs;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.Map;

/**
 * The route for the ER diagrams page.
 */
public class ER extends HTMLTemplateRoute {
    public ER(App app) {
        super(app, "/docs/er");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "docs/er.html";
    }

    @Override
    public Map<String, Object> populateContext(Request request, Response response) throws SQLException {
        return Map.of("database_init", app.readResource("database_init.sql"));
    }
}
