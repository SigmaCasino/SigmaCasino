package io.github.sigmacasino.routes;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

/**
 * The LoginPost class handles the POST request for processing the login form submission.
 */
public class LoginPost extends PostRoute {
    /**
     * The logger for this class.
     */
    private Logger logger = LoggerFactory.getLogger(LoginPost.class);

    /**
     * Constructs a LoginPost route with the specified application.
     *
     * @param app the application instance
     */
    public LoginPost(App app) {
        super(app, "/login");
    }

    /**
     * Handles the POST request for the login form submission. This method checks the user's credentials and logs them
     * in if they are valid. If the user's credentials are invalid, the user is redirected to the login page with an
     * error message.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws SQLException if an SQL error occurs
     */
    @Override
    public void handlePost(Request request, Response response) throws SQLException {
        var params = parseBodyParams(request);
        var email = params.get("email");
        var password = params.get("password");
        if (!email.contains("@") || !email.contains(".") || email.length() < 5 || email.length() > 100
            || password.length() < 8) {
            response.redirect("/login?error=invalid_user");
            return;
        }

        var userCheck = app.getDatabase().prepareStatement(
            "SELECT user_id, username, salt, password_hash FROM users WHERE email = ?"
        );
        userCheck.setString(1, email);
        var userCheckResult = userCheck.executeQuery();
        if (!userCheckResult.next()) {
            response.redirect("/login?error=invalid_user");
            return;
        }

        var salt = userCheckResult.getString("salt");
        var hash = app.hashPassword(password, salt);
        if (!hash.equals(userCheckResult.getString("password_hash"))) {
            response.redirect("/login?error=invalid_user");
            return;
        }

        logger.info("User {} logged in", userCheckResult.getString("username"));
        request.session().attribute("user_id", userCheckResult.getInt("user_id"));
        request.session().maxInactiveInterval(3600);
        request.session().attribute("username", userCheckResult.getString("username"));
        response.redirect("/games");
    }
}
