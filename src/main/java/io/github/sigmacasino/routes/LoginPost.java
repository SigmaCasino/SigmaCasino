package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

public class LoginPost extends PostRoute {
    public LoginPost(App app) {
        super(app, "/login");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        var params = parseBodyParams(request);
        var email = params.get("email");
        var password = params.get("password");
        if (!email.contains("@") || !email.contains(".") || email.length() < 5 || email.length() > 100
            || password.length() < 8) {
            response.redirect("/login?error=invalid_user");
            return Login.ERRORS.get("invalid_user");
        }

        var userCheck =
            app.getDatabase().prepareStatement("SELECT user_id, username, salt, password_hash FROM users WHERE email = ?");
        userCheck.setString(1, email);
        var userCheckResult = userCheck.executeQuery();
        if (!userCheckResult.next()) {
            response.redirect("/login?error=invalid_user");
            return Login.ERRORS.get("invalid_user");
        }
        
        var salt = userCheckResult.getString("salt");
        var hash = app.hashPassword(password, salt);
        if (!hash.equals(userCheckResult.getString("password_hash"))) {
            response.redirect("/login?error=invalid_user");
            return Login.ERRORS.get("invalid_user");
        }

        request.session().attribute("user_id", userCheckResult.getInt("user_id"));
        request.session().attribute("username", userCheckResult.getString("username"));
        response.redirect("/games");
        return null;
    }
}
