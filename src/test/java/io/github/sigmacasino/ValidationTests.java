package io.github.sigmacasino;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.sigmacasino.routes.RegisterPost;

class ValidationTests {
    private App app = new App();

    @Test
    void testEmailValidation() {
        assertTrue(app.checkValidEmail("123@gmail.com"));
        assertFalse(app.checkValidEmail("123gmail.com"));
        assertFalse(app.checkValidEmail("123@gmailcom"));
        assertFalse(app.checkValidEmail("12"));
    }

    @Test
    void testUsernameLengthValidation() {
        var registerPost = new RegisterPost(app);
        assertTrue(registerPost.checkValidUsername("12345678"));
        assertFalse(registerPost.checkValidUsername("12"));
        assertFalse(registerPost.checkValidUsername("loooooooooooooooong"));
    }
}
