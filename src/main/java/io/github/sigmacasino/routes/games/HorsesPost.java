package io.github.sigmacasino.routes.games;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import spark.Request;
import spark.Response;

/**
 * Handles the "/games/horses" POST request, managing user bets, updating the user's balance,
 * and registering a new horse racing game. It also redirects to the replay page with the game results.
 */
public class HorsesPost extends PostRoute {
    /**
     * Initializes the route handler with the app instance and sets the route path.
     *
     * @param app The main application instance.
     */
    public HorsesPost(App app) {
        super(app, "/games/horses");
    }

    /**
     * Handles the POST request for the horse racing game. It validates user balance, generates
     * random race data, stores the result in the database, and updates the user's balance based
     * on the outcome of the game. Redirects the user to the replay page with the race results.
     *
     * @param request The HTTP request containing the game parameters.
     * @param response The HTTP response used to redirect the user.
     * @return Null, as the user is redirected to the replay page.
     * @throws Exception If any errors occur during the request handling.
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        var params = parseBodyParams(request);
        int color = Integer.parseInt(params.get("color"));
        int stake = Integer.parseInt(params.get("stake"));
        Integer user_id = request.session().attribute("user_id");
        if (user_id == null) {
            response.redirect("/login");
            return null;
        }

        var query = app.getDatabase().prepareStatement("SELECT balance WHERE user_id = ?");
        query.setInt(1, user_id);
        var result = query.executeQuery();
        result.next();
        int user_balance = result.getInt(1);
        if (user_balance < stake) {
            response.redirect("/account?error=balance");
            return null;
        }

        Integer[] times = new Integer[4];
        Random random = new SecureRandom();
        List<Integer> i_min_time = new ArrayList<>();
        int min_value = Integer.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            int time = random.nextInt(5_000) + 5_000;  // Random time between 5000 and 9999
            times[i] = time;

            if (time < min_value) {
                min_value = time;
                i_min_time.clear();
                i_min_time.add(i);
            } else if (time == min_value) {
                min_value = time;
                i_min_time.add(i);
            }
        }
        boolean has_won = false;
        if (i_min_time.contains(color)) has_won = true;

        Double[] cubicBezier = new Double[4];
        for (int i = 0; i < 4; i++) {
            cubicBezier[i] = random.nextDouble();
        }
        var post = app.getDatabase().prepareStatement(
            "INSERT INTO horses(user_id, date, bet, guess, times, bezier_curves) VALUES (?,NOW(),?,?,?,?) RETURNING horses_id"
        );
        post.setInt(1, user_id);
        post.setInt(2, stake);
        post.setInt(3, color);
        var unrelevant = app.getDatabase().getConnection().createArrayOf("integer", times);
        post.setArray(4, unrelevant);
        var unrelevant2 = app.getDatabase().getConnection().createArrayOf("double precision", cubicBezier);
        post.setArray(5, unrelevant2);
        var garbage = post.executeQuery();
        garbage.next();
        int horse_id = garbage.getInt(1);
        String kwerenda = has_won ? "+2*" : "-";
        var balance_update =
            app.getDatabase().prepareStatement("UPDATE users SET balance = balance" + kwerenda + "? WHERE user_id = ?");
        balance_update.setInt(1, stake);
        balance_update.setInt(2, user_id);
        balance_update.executeUpdate();
        response.redirect("/games/horses?replay=" + horse_id);
        return null;
    }
}
