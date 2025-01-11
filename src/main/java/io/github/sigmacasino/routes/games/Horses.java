package io.github.sigmacasino.routes.games;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;

import spark.Request;

import java.sql.SQLException;
import java.util.Map;

public class Horses extends HTMLTemplateRoute {
    public Horses(App app) {
        super(app, "/games/horses");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "games/horse_racing.html";
    }

    @Override
    public Map<String, Object> populateContext(Request request) {


        String horses_id = (request.queryParams("replay"));

        try {
            if (horses_id!= null) {
                int horses_id_int = Integer.parseInt(horses_id);
                var statement =
                        app.getDatabase().prepareStatement("SELECT * FROM horses WHERE horses_id = ?");
                statement.setInt(1, horses_id_int);
                var query_result = statement.executeQuery();
                if (query_result.next())
                {
                    return Map.of(
                            "date", query_result.getString("date"),
                            "bet",  query_result.getDouble("bet"),
                            "guess",query_result.getInt("guess"),
                            "times",query_result.getArray("times").getArray(),
                            "bezier_curves", query_result.getArray("bezier_curves").getArray(),
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


