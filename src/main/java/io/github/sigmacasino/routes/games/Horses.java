package io.github.sigmacasino.routes.games;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import java.sql.SQLException;
import java.util.Map;
import spark.Request;
import spark.Response;

/**
 * Handles the "/games/horses" route, fetching horse race data based on a replay ID
 * and populating the HTML template with the race details or an error flag.
 */
public class Horses extends HTMLTemplateRoute {
    /**
     * Initializes the route handler with the app instance and sets the route path.
     *
     * @param app The main application instance.
     */
    public Horses(App app) {
        super(app, "/games/horses");
    }

    /**
     * Returns the path to the HTML template for the horse racing page.
     *
     * @param request The HTTP request.
     * @return The template path.
     */
    @Override
    public String getHTMLTemplatePath(Request request) {
        return "games/horse_racing.html";
    }

    /**
     * Populates the template context with race data or an error flag based on the replay ID.
     *
     * @param request The HTTP request containing the query parameter.
     * @param response The HTTP response used to redirect the user.
     * @return A map with race data or an error flag.
     */
    @Override
    public Map<String, Object> populateContext(Request request, Response response) throws SQLException {
        String horses_id = request.queryParams("replay");

        if (horses_id != null) {
            int horses_id_int = Integer.parseInt(horses_id);
            var statement = app.getDatabase().prepareStatement("SELECT * FROM horses WHERE horses_id = ?");
            statement.setInt(1, horses_id_int);
            var query_result = statement.executeQuery();
            if (query_result.next()) {
                return Map.of(
                    "date",
                    query_result.getString("date"),
                    "bet",
                    query_result.getDouble("bet"),
                    "guess",
                    query_result.getInt("guess"),
                    "times",
                    query_result.getArray("times").getArray(),
                    "bezier_curves",
                    query_result.getArray("bezier_curves").getArray()
                );
            } else {
                response.redirect(path + "?error=wrong_replay_id");
            }
        }
        return Map.of();
    }

    @Override
    public Map<String, String> getNotificationDefinitions() {
        return Map.of("wrong_replay_id", "The replay ID provided is invalid.");
    }
}
