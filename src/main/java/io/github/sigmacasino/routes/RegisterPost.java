package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import java.security.SecureRandom;
import java.util.Random;
import spark.Request;
import spark.Response;

public class RegisterPost extends PostRoute {
    private Random saltGenerator = new SecureRandom();

    public RegisterPost(App app) {
        super(app, "/register");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        var params = parseBodyParams(request);
        var username = params.get("username");
        var email = params.get("email");
        var password = params.get("password");
        if (username.length() < 3 || username.length() > 16) {
            response.redirect("/register?error=username_length");
            return Register.ERRORS.get("username_length");
        }
        if (!email.contains("@") || !email.contains(".") || email.length() < 5 || email.length() > 100) {
            response.redirect("/register?error=email_invalid");
            return Register.ERRORS.get("email_invalid");
        }
        if (password.length() < 8) {
            response.redirect("/register?error=password_length");
            return Register.ERRORS.get("password_length");
        }

        var existingUserCheck =
            app.getDatabase().prepareStatement("SELECT user_id FROM users WHERE username = ? OR email = ?");
        existingUserCheck.setString(1, username);
        existingUserCheck.setString(2, email);
        var existingUserCheckResult = existingUserCheck.executeQuery();
        if (existingUserCheckResult.next()) {
            response.redirect("/register?error=user_exists");
            return Register.ERRORS.get("user_exists");
        }

        var insertUser = app.getDatabase().prepareStatement(
            "INSERT INTO users (username, salt, email, password_hash) VALUES (?, ?, ?, ?)"
        );
        var salt = generateSalt();
        var hash = app.hashPassword(password, salt);
        insertUser.setString(1, username);
        insertUser.setString(2, salt);
        insertUser.setString(3, email);
        insertUser.setString(4, hash);
        insertUser.executeUpdate();
        System.out.println(username + salt + email + hash);

        response.redirect("/login?error=registered");
        return null;
    }

    private String generateSalt() {
        var salt = new byte[16];
        saltGenerator.nextBytes(salt);
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
