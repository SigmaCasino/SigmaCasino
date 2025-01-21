package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import spark.Request;
import spark.Response;

/**
 * The Account class handles the account route, displaying the user's account information.
 */
public class Account extends HTMLTemplateRoute {
    /**
     * Constructs an Account route with the specified application.
     *
     * @param app the application instance
     */
    public Account(App app) {
        super(app, "/account");
        this.loginRequired = true;
    }

    /**
     * Returns the path to the HTML template for the account page.
     *
     * @param request the HTTP request
     * @return the path to the HTML template
     */
    @Override
    public String getHTMLTemplatePath(Request request) {
        return "account.html";
    }

    /**
     * Populates the context for the account page with the user's balance as "balance" and recent transactions,
     * including deposits, withdrawals and game results, as a submap containing operation type, identifier, date and
     * amount.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return a map containing the user's balance and transactions
     * @throws SQLException if an SQL error occurs
     */
    @Override
    public Map<String, Object> populateContext(Request request, Response response) throws SQLException {
        Integer userId = request.session().attribute("user_id");

        String balanceQuery = "SELECT balance FROM users WHERE user_id = ?";

        String transactionsQuery =
            "SELECT * FROM (SELECT 'transaction' as operation, transaction_id, date, amount FROM deposits_withdrawals WHERE user_id = ? "
            + "UNION SELECT 'horses' as operation, horses_id, date, bet FROM horses WHERE user_id = ? "
            + "UNION SELECT 'roulette' as operation, roulette_id, date, bet FROM roulette WHERE user_id = ?) ORDER BY date DESC LIMIT 50";

        int balanceInteger = 0;
        try (var balance = app.getDatabase().prepareStatement(balanceQuery)) {
            balance.setInt(1, userId);
            var balanceResult = balance.executeQuery();
            balanceResult.next();
            balanceInteger = balanceResult.getInt("balance");
        }

        try (var transactions = app.getDatabase().prepareStatement(transactionsQuery)) {
            for (int i = 0; i < 3; i++) {
                transactions.setInt(i + 1, userId);
            }
            var transactionsResult = transactions.executeQuery();

            // var rawDate = transactionsResult.getString("date");
            
            // var formattedDate = "";
            // if(rawDate != null) {
            //     var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Input format
            //     var date = sdf.parse(rawDate);
            //     var outputFormat = new SimpleDateFormat("MMMM d, yyyy");
            //     var formattedDate = outputFormat.format(date);
            // }
 

            ArrayList<Map<String, ? extends Serializable>> transactionsList = new ArrayList<>();
            while (transactionsResult.next()) {
                var transaction = Map.of(
                    "operation",
                    transactionsResult.getString("operation"),
                    "id",
                    transactionsResult.getInt("transaction_id"),
                    "date",
                    transactionsResult.getString("date"),
                    "amount",
                    transactionsResult.getInt(4)
                );
                transactionsList.add(transaction);
            }
            return Map.of("balance", balanceInteger, "transactions", transactionsList);
        }
    }

    @Override
    public Map<String, String> getNotificationDefinitions() {
        return Map.of(
            "balance",
            "Your balance is insufficient for this bet.",
            "password_changed",
            "Password changed successfully.",
            "payment",
            "Payment successful. Your balance should be updated shortly.",
            "payment_failed",
            "Payment failed. Please contact our support."
        );
    }
}
