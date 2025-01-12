package io.github.sigmacasino.routes.games;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import spark.Request;

import java.sql.SQLException;
import java.util.Map;

public class Roulette extends HTMLTemplateRoute {

    public Roulette(App app) {
        super(app, "/games/roulette");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "games/roulette.html";
    }

    @Override
    public Map<String, Object> populateContext(Request request) {
         // TODO jęśli replay, to pobrać dane z DB i wrzucić jakoś sensownie do tej mapy

        String roulette_id = (request.queryParams("replay"));

        try {
            if (roulette_id!= null) {
                int horses_id_int = Integer.parseInt(roulette_id);
                var statement =
                        app.getDatabase().prepareStatement("SELECT * FROM roulette WHERE roulette_id = ?");
                statement.setInt(1, horses_id_int);
                var query_result = statement.executeQuery();
                if (query_result.next())
                {
                    String litera = query_result.getString("guess");
                    litera = switch (litera) {
                        case "b" -> "black";
                        case "r" -> "red";
                        case "g" -> "green";
                        default -> litera;
                    };
                    return Map.of(
                            "date", query_result.getString("date"),
                            "bet",  query_result.getDouble("bet"),
                            "guess",litera,
                            "result",query_result.getInt("times"),
                            "error", false);
                }
                else
                {
                    return Map.of("error", true);
                }
            }

        } catch (SQLException e) {
            return Map.of("error", true);
        }


        return Map.of("error", false);
    }
}
