package io.github.sigmacasino;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.sigmacasino.routes.RegisterPost;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Random;

public class HelperMethodsTests {

    App app = new App();

    @Test
    public void testHashPassword() {
        var password = "password123";
        var salt = "randomSalt";
        var hash = app.hashPassword(password, salt);
        assertNotNull(hash);
        assertEquals(hash, app.hashPassword(password, salt));
    }

    @Test
    public void testGenerateSalt() {
        var salt = (new SecitityUtilis()).generateSalt();
        assertNotNull(salt);
        assertEquals(16, salt.length());
    }
}

