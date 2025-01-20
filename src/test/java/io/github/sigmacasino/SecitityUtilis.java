package io.github.sigmacasino;

import java.security.SecureRandom;
import java.util.Random;

public class SecitityUtilis {
    Random saltGenerator = new SecureRandom();

    String generateSalt() {
        var salt = new byte[16];
        saltGenerator.nextBytes(salt);
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
