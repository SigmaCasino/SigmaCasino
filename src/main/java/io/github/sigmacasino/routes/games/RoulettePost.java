package io.github.sigmacasino.routes.games;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import java.security.SecureRandom;
import java.util.*;
import spark.Request;
import spark.Response;

/**
 * Handles the "/games/roulette" POST request, processing user bets, updating the user's balance,
 * and registering a new roulette game result. Redirects the user to the replay page with the game results.
 */
public class RoulettePost extends PostRoute {
    /**
     * A map of roulette numbers and their corresponding colors.
     */
    private Map<Integer, String> rouletteItems = new HashMap<>();

    /**
     * Initializes the route handler with the app instance and sets the route path.
     * Also initializes the roulette items map with predefined colors for each number.
     *
     * @param app The main application instance.
     */
    public RoulettePost(App app) {
        super(app, "/games/roulette");
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
    }

    /**
     * Handles the POST request for the roulette game. It validates the user's balance,
     * simulates the roulette spin, stores the result in the database, and updates the user's balance.
     * Finally, it redirects the user to the replay page with the game result.
     *
     * @param request The HTTP request containing the game parameters.
     * @param response The HTTP response used to redirect the user.
     * @return Null, as the user is redirected to the replay page.
     * @throws Exception If any errors occur during the request handling.
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Random random = new SecureRandom();
        int number = (int) Math.floor(random.nextDouble() * 37);

        var params = parseBodyParams(request);
        String color = params.get("color");
        int stake = Integer.parseInt(params.get("stake"));
        Integer integer_user_id = request.session().attribute("user_id");
        if (integer_user_id == null) {
            response.redirect("/login");
            return null;
        }
        int user_id = integer_user_id;

        var query = app.getDatabase().prepareStatement("SELECT balance WHERE user_id = ?");
        query.setInt(1, user_id);
        var result = query.executeQuery();
        result.next();
        int user_balance = result.getInt(1);
        if (user_balance < stake) {
            response.redirect("/account?error=balance");
            return null;
        }

        var post = app.getDatabase().prepareStatement(
            "INSERT INTO roulette(user_id, date, bet, guess, result) VALUES (?,NOW(),?,?,?) RETURNING roulette_id"
        );
        post.setInt(1, user_id);
        post.setInt(2, stake);
        post.setString(3, color.substring(0, 1));
        post.setInt(4, number);
        var garbage = post.executeQuery();
        garbage.next();
        int roulette_id = garbage.getInt(1);
        boolean has_won = false;
        if (color.equals(rouletteItems.get(number))) {
            has_won = true;
        }

        String kwerenda = has_won ? "+" : "-";
        if (has_won && color.equals("green")) {
            kwerenda = "+30*";
        }
        var balance_update =
            app.getDatabase().prepareStatement("UPDATE users SET balance = balance" + kwerenda + "? WHERE user_id = ?");
        balance_update.setInt(1, stake);
        balance_update.setInt(2, user_id);
        balance_update.executeUpdate();
        response.redirect("/games/roulette?replay=" + roulette_id);
        return null;
    }
}
