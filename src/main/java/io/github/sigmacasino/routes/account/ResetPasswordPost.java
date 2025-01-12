package io.github.sigmacasino.routes.account;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import spark.Request;
import spark.Response;

/**
 * The ResetPasswordPost class handles the POST request for resetting a user's password.
 */
public class ResetPasswordPost extends PostRoute {
    /**
     * Constructs a ResetPasswordPost route with the specified application.
     *
     * @param app the application instance
     */
    public ResetPasswordPost(App app) {
        super(app, "/account/reset_password");
    }

    /**
     * Handles the POST request to reset the user's password.
     * If the user is not logged in, they are redirected to the login page.
     * If the old password is incorrect, the user is redirected to the reset password page with an error message.
     * Otherwise, the user's password is updated and they are redirected to the account page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return null
     * @throws Exception if an error occurs during password reset
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        if (request.session().attribute("user_id") == null) {
            response.redirect("/login");
            return null;
        }

        Integer userId = request.session().attribute("user_id");
        var saltCheck = app.getDatabase().prepareStatement("SELECT salt, password_hash FROM users WHERE user_id = ?");
        saltCheck.setInt(1, userId);

        var saltCheckResult = saltCheck.executeQuery();
        saltCheckResult.next();
        var salt = saltCheckResult.getString("salt");

        var params = parseBodyParams(request);
        var newPassword = params.get("new_password");
        var oldPassword = params.get("old_password");

        var newPasswordHash = app.hashPassword(newPassword, salt);
        var oldPasswordHash = app.hashPassword(oldPassword, salt);

        if (!oldPasswordHash.equals(saltCheckResult.getString("password_hash"))) {
            response.redirect("/account/reset_password?error=invalid_password");
            return null;
        }

        var updatePassword =
                app.getDatabase().prepareStatement("UPDATE users SET password_hash = ? WHERE user_id = ?");
        updatePassword.setString(1, newPasswordHash);
        updatePassword.setInt(2, userId);
        updatePassword.executeUpdate();

        response.redirect("/account");
        return null;
    }
}