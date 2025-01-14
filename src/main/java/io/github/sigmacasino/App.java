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

import io.github.sigmacasino.routes.games.Horses;
import io.github.sigmacasino.routes.games.HorsesPost;
import io.github.sigmacasino.routes.games.Roulette;
import io.github.sigmacasino.routes.games.RoulettePost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

/**
 * The main class of the Sigma Casino application.
 * This class initializes the database, Spark, Stripe, and routes.
 * It also provides utility methods for reading resources and hashing passwords.
 */
public class App {
    /**
     * The domain of the application, e.g. "sigmacasino.fly.dev".
     * If the PUBLIC_IP environment variable is not set, the domain will default to "localhost".
     */
    private String domain = System.getenv("PUBLIC_IP");
    /**
     * The port on which the application will listen for incoming connections.
     * The default value is 6969.
     */
    private int port = 6_969;

    /**
     * An array of routes that the application will handle.
     * Each route is an instance of a class that extends the HTTPRoute class.
     */
    private HTTPRoute[] routes = {
        new Root(this),         new Index(this), new Login(this),         new LoginPost(this),   new Register(this),
        new RegisterPost(this), new Games(this), new StripeDeposit(this), new StripeResult(this),
        new Horses(this),
        new HorsesPost(this),
            new Roulette(this), new RoulettePost(this)
    };

    /**
     * The logger for this class.
     */
    private Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * The database connection used by the application.
     */
    private LocalDatabase db;
    /**
     * The Jinjava template engine used by the application.
     */
    private Jinjava jinjava = new Jinjava();
    /**
     * The SHA-256 password hasher used by the application.
     */
    private MessageDigest passwordHasher;

    /**
     * Initializes the application by setting up the database, Spark, Stripe, and routes.
     * This method should be called once at the beginning of the application's lifecycle.
     */
    public void run() {
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

    /**
     * Initializes the database connection using the environment variables.
     * If the POSTGRES_PASSWORD environment variable is not set, the password will default to an empty string.
     * The database is initialized by running the "database_init.sql" script.
     */
    private void initializeDatabase() {
        var password = System.getenv("POSTGRES_PASSWORD");
        if (password == null) {
            logger.error("No password for the database has been set in the environment!");
            password = "";
        }
        db = new LocalDatabase(5_432, "sigmacasino", password, "sigmacasino");
        db.runScript(readResource("database_init.sql"));
    }

    /**
     * Initializes the Spark web server by setting the port and the static files location.
     */
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

    /**
     * Registers all routes in the routes array with Spark.
     * Each route is registered by calling the registerSparkRoute method.
     * 
     * @see HTTPRoute#registerSparkRoute()
     */
    private void addRoutes() {
        for (var route : routes) {
            route.registerSparkRoute();
            logger.info("Registered route: {}", route.path);
        }
    }

    /**
     * Reads a resource file from the resources directory and returns its contents as a string.
     * If the file is not found, an error message is logged and an empty string is returned.
     * 
     * @param fileName the name of the file to read
     * @return the contents of the file as a string
     */
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

    /**
     * Returns the database connection used by the application.
     * 
     * @return the database connection
     */
    public LocalDatabase getDatabase() {
        return db;
    }

    /**
     * Returns the Jinjava template engine used by the application.
     * 
     * @return the Jinjava template engine
     */
    public Jinjava getJinjava() {
        return jinjava;
    }

    /**
     * Returns the domain of the application, e.g. "http://sigmacasino.fly.dev".
     * 
     * @return the domain of the application
     */
    public String getDomain() {
        return "http://" + domain;
    }

    /**
     * Hashes a password using the SHA-256 algorithm and a salt.
     * The salt is prepended to the password before hashing.
     * @param password the password to hash
     * @param salt the salt to prepend to the password
     * @return the hashed password as a hexadecimal lowercase string
     */
    public String hashPassword(String password, String salt) {
        password = salt + password;
        var bytes = passwordHasher.digest(password.getBytes(StandardCharsets.UTF_8));
        var sb = new StringBuilder();
        for (var b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * The main method of the application.
     * It sets the name of the main thread and creates an instance of the App class.
     * The run method is then called on the instance to initialize the application.
     * 
     * @param args the command-line arguments, currently unused
     */
    public static void main(String[] args) {
        Thread.currentThread().setName("Main thread");
        new App().run();
    }
}
