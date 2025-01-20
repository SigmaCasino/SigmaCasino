package io.github.sigmacasino;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.github.sigmacasino.routes.RegisterPost;

class SecurityMethodsTests {
    private App app = new App();

    @Test
    void testHashPassword() {
        var password = "password123";
        var salt = "randomSalt";
        var hash = app.hashPassword(password, salt);
        assertNotNull(hash);
        assertEquals(hash, app.hashPassword(password, salt));
    }

    @Test
    void testGenerateSalt() {
        var salt = new RegisterPost(app).generateSalt();
        assertNotNull(salt);
        assertEquals(32, salt.length());
    }
}

