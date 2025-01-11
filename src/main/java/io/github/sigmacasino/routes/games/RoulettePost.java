package io.github.sigmacasino.routes.games;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

import java.security.SecureRandom;
import java.util.*;

public class RoulettePost extends PostRoute {

    public RoulettePost(App app) {
        super(app, "/games/roulette");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<Integer, String> rouletteItems = new HashMap<>();
        rouletteItems.put(0, "green");
        rouletteItems.put(32, "red");
        rouletteItems.put(15, "black");
        rouletteItems.put(19, "red");
        rouletteItems.put(4, "black");
        rouletteItems.put(21, "red");
        rouletteItems.put(2, "black");
        rouletteItems.put(25, "red");
        rouletteItems.put(17, "black");
        rouletteItems.put(34, "red");
        rouletteItems.put(6, "black");
        rouletteItems.put(27, "red");
        rouletteItems.put(13, "black");
        rouletteItems.put(36, "red");
        rouletteItems.put(11, "black");
        rouletteItems.put(30, "red");
        rouletteItems.put(8, "black");
        rouletteItems.put(23, "red");
        rouletteItems.put(10, "black");
        rouletteItems.put(5, "red");
        rouletteItems.put(24, "black");
        rouletteItems.put(16, "red");
        rouletteItems.put(33, "black");
        rouletteItems.put(1, "red");
        rouletteItems.put(20, "black");
        rouletteItems.put(14, "red");
        rouletteItems.put(31, "black");
        rouletteItems.put(9, "red");
        rouletteItems.put(22, "black");
        rouletteItems.put(18, "red");
        rouletteItems.put(29, "black");
        rouletteItems.put(7, "red");
        rouletteItems.put(28, "black");
        rouletteItems.put(12, "red");
        rouletteItems.put(35, "black");
        rouletteItems.put(3, "red");
        rouletteItems.put(26, "black");

        Random random = new SecureRandom();
        int number =

        var params = parseBodyParams(request);
        String color = params.get("color");
        int stake = Integer.parseInt(params.get("stake"));
        String string_user_id = request.session().attribute("user_id");
        if (string_user_id == null)
        {
            response.redirect("/login");
            return null;
        }
        int user_id = Integer.parseInt(string_user_id);

        Integer [] times = new Integer[4];




        var post = app.getDatabase().prepareStatement("INSERT INTO horses(user_id, date, bet, guess integer, times, bezier_curves) VALUES (?,NOW(),?,?,?,?) RETURNING horses_id");
        post.setInt(1,user_id);
        post.setInt(2,stake);
        post.setInt(3,color);
        var unrelevant = app.getDatabase().getConnection().createArrayOf("integer",times);
        post.setArray(4, unrelevant);
        var unrelevant2 = app.getDatabase().getConnection().createArrayOf("double precision",cubicBezier);
        post.setArray(5,unrelevant2);
        int horse_id = post.executeQuery().getInt(1);

        String kwerenda = has_won ? "+2*" : "-";
        var balance_update = app.getDatabase().prepareStatement("UPDATE users SET balance = balance" + kwerenda + "? WHERE user_id = ?");
        balance_update.setInt(1,stake);
        balance_update.setInt(2,user_id);
        balance_update.executeUpdate();
        response.redirect("/games/horses?replay="+horse_id);
        return null; // TODO zarejestrować nową grę do DB i przekierować użytkownika na replay



















        return null;  // TODO zarejestrować nową grę do DB i przekierować użytkownika na replay
    }
}
