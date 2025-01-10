package io.github.sigmacasino;

import com.hubspot.jinjava.Jinjava;
import com.stripe.Stripe;
import io.github.sigmacasino.routes.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import io.github.sigmacasino.routes.account.StripeDeposit;
import io.github.sigmacasino.routes.account.StripeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class App {
    private String domain = System.getenv("PUBLIC_IP");
    private int port = 6969;

    private HTTPRoute[] routes = {
        new Root(this),
        new Index(this),
        new StripeDeposit(this),
        new StripeResult(this)
    };

    private Logger logger = LoggerFactory.getLogger(getClass());
    private LocalDatabase db;

    private Jinjava jinjava = new Jinjava();

    private void run() {
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

    public static void main(String[] args) {
        Thread.currentThread().setName("Main thread");
        new App().run();
    }
}
