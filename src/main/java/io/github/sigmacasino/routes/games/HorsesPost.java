package io.github.sigmacasino.routes.games;

import com.stripe.model.tax.Registration;
import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HorsesPost extends PostRoute {

    public HorsesPost(App app) {
        super(app, "/games/horses");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        var params = parseBodyParams(request);
        int color = Integer.parseInt(params.get("color"));
        int stake = Integer.parseInt(params.get("stake"));
        Integer user_id = request.session().attribute("user_id");
        if (user_id == null)
        {
            response.redirect("/login");
            return null;
        }


        var query  = app.getDatabase().prepareStatement("SELECT balance WHERE user_id = ?");
        query.setInt(1,user_id);
        var result = query.executeQuery();
        result.next();
        int user_balance = result.getInt(1);
        if(user_balance<stake)
        {
            response.redirect("/account?error=balance");
            return null;
        }


        Integer [] times = new Integer[4];
        Random random = new SecureRandom();
        List<Integer> i_min_time = new ArrayList<>();
        int min_value = Integer.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            int time = random.nextInt(5000) + 5000; // Random time between 5000 and 9999
            times[i] = time;

            if(time < min_value)
            {
                min_value = time;
                i_min_time.clear();
                i_min_time.add(i);
            }
            else if (time == min_value)
            {
                min_value = time;
                i_min_time.add(i);
            }
        }
        boolean has_won = false;
        if (i_min_time.contains(color))
            has_won = true;


        Double [] cubicBezier = new Double [4];
        for(int i=0; i<4; i++)
        {
            cubicBezier[i] = random.nextDouble();
        }
        var post = app.getDatabase().prepareStatement("INSERT INTO horses(user_id, date, bet, guess, times, bezier_curves) VALUES (?,NOW(),?,?,?,?) RETURNING horses_id");
        post.setInt(1,user_id);
        post.setInt(2,stake);
        post.setInt(3,color);
        var unrelevant = app.getDatabase().getConnection().createArrayOf("integer",times);
        post.setArray(4, unrelevant);
        var unrelevant2 = app.getDatabase().getConnection().createArrayOf("double precision",cubicBezier);
        post.setArray(5,unrelevant2);
        var garbage = post.executeQuery();
        garbage.next();
        int horse_id = garbage.getInt(1);
        String kwerenda = has_won ? "+2*" : "-";
        var balance_update = app.getDatabase().prepareStatement("UPDATE users SET balance = balance" + kwerenda + "? WHERE user_id = ?");
        balance_update.setInt(1,stake);
        balance_update.setInt(2,user_id);
        balance_update.executeUpdate();
        response.redirect("/games/horses?replay="+horse_id);
        return null; // TODO zarejestrować nową grę do DB i przekierować użytkownika na replay
    }
}
