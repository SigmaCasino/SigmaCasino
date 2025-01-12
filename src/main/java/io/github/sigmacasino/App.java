package io.github.sigmacasino;

import com.hubspot.jinjava.Jinjava;
import com.stripe.Stripe;
import io.github.sigmacasino.routes.*;
import io.github.sigmacasino.routes.account.StripeDeposit;
import io.github.sigmacasino.routes.account.StripeResult;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class App {
    private String domain = System.getenv("PUBLIC_IP");
    private int port = 6_969;

    private HTTPRoute[] routes = {
        new Root(this),         new Index(this), new Login(this),         new LoginPost(this),   new Register(this),
        new RegisterPost(this), new Games(this), new StripeDeposit(this), new StripeResult(this), new Account(this)
    };

    private Logger logger = LoggerFactory.getLogger(getClass());
    private LocalDatabase db;

    private Jinjava jinjava = new Jinjava();

    private MessageDigest passwordHasher;

    private void run() {
        try {
            passwordHasher = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Failed to obtain SHA-256 hasher!", e);
            return;
        }
        initializeDatabase();
        initializeSpark();
        initializeStripe();
        addRoutes();
    }

    private void initializeDatabase() {
        var password = System.getenv("POSTGRES_PASSWORD");
        if (password == null) {
            logger.error("No password for the database has been set in the environment!");
            password = "";
        }
        db = new LocalDatabase(5_432, "sigmacasino", password, "sigmacasino");
        db.runScript(readResource("database_init.sql"));
    }

    private void initializeSpark() {
        Spark.staticFiles.location("/static");
        Spark.port(port);
    }

    /**
     * Initializes the Stripe API with the secret key generated in the Dashboard.
     * Also ensures that domain is set to a valid value for the Stripe API.
     */
    private void initializeStripe() {
        Stripe.apiKey = System.getenv("STRIPE_KEY");
        if (domain == null) {
            domain = "localhost:" + port;
        }
    }

    private void addRoutes() {
        for (var route : routes) {
            route.registerSparkRoute();
            logger.info("Registered route: {}", route.path);
        }
    }

    public String readResource(String fileName) {
        InputStream inputStream = getClass().getResourceAsStream("/" + fileName);

        if (inputStream == null) {
            logger.error("Failed to read resource file: {}", fileName);
            return "";
        }

        Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
        String html = scanner.useDelimiter("\\A").next();
        scanner.close();
        return html;
    }

    public LocalDatabase getDatabase() {
        return db;
    }

    public Jinjava getJinjava() {
        return jinjava;
    }

    public String getDomain() {
        return "http://" + domain;
    }

    public String hashPassword(String password, String salt) {
        password = salt + password;
        var bytes = passwordHasher.digest(password.getBytes(StandardCharsets.UTF_8));
        var sb = new StringBuilder();
        for (var b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("Main thread");
        new App().run();
    }
}
