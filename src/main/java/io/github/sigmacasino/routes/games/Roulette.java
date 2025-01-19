package io.github.sigmacasino.routes.games;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import java.sql.SQLException;
import java.util.Map;
import spark.Request;
import spark.Response;
/**
 * Handles the "/games/roulette" route, fetching roulette game data based on a replay ID
 * and populating the HTML template with the game details or an error flag.
 */
public class Roulette extends HTMLTemplateRoute {
    /**
     * Initializes the route handler with the app instance and sets the route path.
     *
     * @param app The main application instance.
     */
    public Roulette(App app) {
        super(app, "/games/roulette");
    }

    /**
     * Returns the path to the HTML template for the roulette game page.
     *
     * @param request The HTTP request.
     * @return The template path.
     */
    @Override
    public String getHTMLTemplatePath(Request request) {
        return "games/roulette.html";
    }

    /**
     * Populates the template context with roulette game data (date, bet amount, guess, result)
     * or redirects the user on error with a correct flag in the URL.
     *
     * @param request The HTTP request containing the query parameter.
     * @param response The HTTP response used to redirect the user.
     * @return A map with game data.
     */
    @Override
    public Map<String, Object> populateContext(Request request, Response response) throws SQLException {
        String roulette_id = request.queryParams("replay");

        if (roulette_id != null) {
            int horses_id_int = Integer.parseInt(roulette_id);
            var statement = app.getDatabase().prepareStatement("SELECT * FROM roulette WHERE roulette_id = ?");
            statement.setInt(1, horses_id_int);
            var query_result = statement.executeQuery();
            if (query_result.next()) {
                String litera = query_result.getString("guess");
                litera = switch (litera) {
                    case "b" -> "black";
                    case "r" -> "red";
                    case "g" -> "green";
                    default -> litera;
                };
                return Map.of(
                    "date", query_result.getString("date"),
                    "bet", query_result.getDouble("bet"),
                    "guess", litera,
                    "result", query_result.getInt("result")
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
