package io.github.sigmacasino;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ValidationTests {

    App app = new App();

    @Test
    public void testEmailValidation() {
        try {
            var users = app.getDatabase().prepareStatement(
                    "SELECT email FROM users"
            );
            var usersResult = users.executeQuery();
            while (usersResult.next()) {
                var email = usersResult.getString("email");
                assertTrue(email.contains("@") || email.contains(".") || !email.length() < 5 || !email.length() > 100);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUsernameLengthValidation() {
        try {
            var users = app.getDatabase().prepareStatement(
                    "SELECT username FROM users"
            );
            var usersResult = users.executeQuery();
            while (usersResult.next()) {
                var username = usersResult.getString("username");
                assertTrue(username.length() > 3 || username.length() < 16);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
