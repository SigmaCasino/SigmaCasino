package io.github.sigmacasino.routes;

import io.github.sigmacasino.App;
import io.github.sigmacasino.HTMLTemplateRoute;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import spark.Request;

public class Account extends HTMLTemplateRoute {
    public Account(App app) {
        super(app, "/account");
    }

    @Override
    public String getHTMLTemplatePath(Request request) {
        return "account.html";
    }

    @Override
    public Map<String, Object> populateContext(Request request) {
        if (request.session().attribute("user_id") == null) {
//            response.redirect("/login");
            return null;
        }

        var userId = Integer.parseInt(request.session().attribute("user_id"));

        String balanceQuery = "SELECT balance FROM casino_database.users WHERE user_id = ?";

        String transactionsQuery =
                "SELECT * FROM (SELECT 'transaction' as operation, transaction_id, date, amount FROM casino_database.deposits_withdrawals WHERE user_id = ?" +
                        "UNION SELECT 'horses' as operation, horses_id, date, bet FROM casino_database.horses WHERE user_id = ?" +
                        "UNION SELECT roulette_id, date, bet FROM casino_database.roulette WHERE user_id = ?" +
                        "UNION SELECT 'roulette' as operation, roulette_id, date, bet FROM casino_database.users WHERE user_id = ?) ORDER BY date DESC LIMIT 50;";

        int balanceInteger = 0;
        try (var balance = app.getDatabase().prepareStatement(balanceQuery)) {
            balance.setInt(1, userId);
            var balanceResult = balance.executeQuery();
            balanceInteger = balanceResult.getInt("balance");
        } catch (SQLException e) {
            e.printStackTrace();
//            response.redirect("/login");
            return null;
        }

        try (var transactions = app.getDatabase().prepareStatement(transactionsQuery)) {
            for (int i = 0; i < 4; i ++) {
                transactions.setInt(i + 1, userId);
            }
            var transactionsResult = transactions.executeQuery();
            var transactionsList = new ArrayList<Map<String, Object>>();
            while (transactionsResult.next()) {
                var transaction = Map.of(
                        "operation", transactionsResult.getString("operation"),
                        "id", transactionsResult.getInt("transaction_id"),
                        "date", transactionsResult.getString("date"),
                        "amount", transactionsResult.getInt("amount"),
                        "bet", transactionsResult.getInt("bet")
                );
                transactionsList.add(transaction);
            }
            return Map.of("balance", balanceInteger, "transactions", transactionsList);
        } catch (SQLException e) {
            e.printStackTrace();
//            response.redirect("/login");
            return null;
        }

        return Map.of();
    }
}
