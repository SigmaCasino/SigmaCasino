package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

/**
 * The LoginPost class handles the POST request for processing the login form submission.
 */
public class LoginPost extends PostRoute {
    /**
     * Constructs a LoginPost route with the specified application.
     *
     * @param app the application instance
     */
    public LoginPost(App app) {
        super(app, "/login");
    }

    /**
     * Handles the POST request for the login form submission. This method checks the user's credentials and logs them in if they are valid.
     * If the user's credentials are invalid, the user is redirected to the login page with an error message.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return an object representing the result of the request handling
     * @throws Exception if an error occurs during request handling
     */
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
