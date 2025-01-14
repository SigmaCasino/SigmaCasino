package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Random;
import spark.Request;
import spark.Response;

/**
 * The RegisterPost class handles the POST request for processing the registration form submission.
 */
public class RegisterPost extends PostRoute {
    /**
     * A random number generator for generating salts.
     */
    private Random saltGenerator = new SecureRandom();

    /**
     * Constructs a RegisterPost route with the specified application.
     *
     * @param app the application instance
     */
    public RegisterPost(App app) {
        super(app, "/register");
    }

    /**
     * Handles the POST request for the registration form submission. This method validates the input,
     * checks for existing users, and registers a new user if the input is valid.
     * If the input is invalid or a user already exists, the user is redirected to the registration page with an error
     * message.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return an object representing the result of the request handling
     * @throws Exception if an error occurs during request handling
     */
    @Override
    public void handlePost(Request request, Response response) throws SQLException {
        var params = parseBodyParams(request);
        var username = params.get("username");
        var email = params.get("email");
        var password = params.get("password");
        if (username.length() < 3 || username.length() > 16) {
            response.redirect("/register?error=username_length");
            return;
        }
        if (!email.contains("@") || !email.contains(".") || email.length() < 5 || email.length() > 100) {
            response.redirect("/register?error=email_invalid");
            return;
        }
        if (password.length() < 8) {
            response.redirect("/register?error=password_length");
            return;
        }

        var existingUserCheck =
            app.getDatabase().prepareStatement("SELECT user_id FROM users WHERE username = ? OR email = ?");
        existingUserCheck.setString(1, username);
        existingUserCheck.setString(2, email);
        var existingUserCheckResult = existingUserCheck.executeQuery();
        if (existingUserCheckResult.next()) {
            response.redirect("/register?error=user_exists");
            return;
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
        System.out.println(username + salt + email + hash);  // TODO

        response.redirect("/login?error=registered");
    }

    /**
     * Generates a random salt for password hashing.
     * The salt is a 16-byte array encoded as a hexadecimal string.
     *
     * @return a string representing the generated salt
     */
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
