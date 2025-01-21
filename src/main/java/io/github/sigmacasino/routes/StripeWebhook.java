package io.github.sigmacasino.routes;

import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionRetrieveParams;
import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

/**
 * A route that listens for Stripe events.
 */
public class StripeWebhook extends PostRoute {
    /**
     * The webhook secret used to verify events.
     */
    private String webhookSecret = System.getenv("STRIPE_WEBHOOK_SECRET");
    /**
     * The logger used to log events from this class.
     */
    private Logger logger = LoggerFactory.getLogger(getClass());

    public StripeWebhook(App app) {
        super(app, "/webhook/stripe");
    }

    /**
     * Handles a POST request to the Stripe webhook endpoint.
     * Verifies the event and fulfills the checkout session if it is completed.
     * @see <a href="https://stripe.com/docs/payments/checkout/fulfillment">Fulfilling Checkout Sessions</a>
     * @param request The HTTP request.
     * @param response The HTTP response.
     */
    @Override
    public void handlePost(Request request, Response response) throws SQLException {
        String payload = request.body();
        String sigHeader = request.headers("Stripe-Signature");
        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (Exception e) {
            response.status(400);
            return;
        }

        if ("checkout.session.completed".equals(event.getType())
            || "checkout.session.async_payment_succeeded".equals(event.getType())) {
            Session sessionEvent = (Session) event.getDataObjectDeserializer().getObject().get();
            try {
                fulfillCheckout(sessionEvent.getId());
            } catch (StripeException e) {
                response.status(500);
                return;
            }
        }

        response.status(200);
    }

    /**
     * Fulfills a Checkout Session if it is completed.
     * Updates the user's balance and logs the deposit in the database.
     * @param sessionId The ID of the Checkout Session to fulfill.
     * @throws StripeException If an error occurs while fulfilling the Checkout Session.
     */
    private void fulfillCheckout(String sessionId) throws StripeException, SQLException {
        logger.debug("Fulfilling checkout session {}", sessionId);

        // Retrieve the Checkout Session from the API with line_items expanded
        SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("line_items").build();

        Session checkoutSession = Session.retrieve(sessionId, params, null);

        // Check the Checkout Session's payment_status property to determine if fulfillment should be performed
        if (!"unpaid".equals(checkoutSession.getPaymentStatus())) {
            var userId = Integer.parseInt(checkoutSession.getMetadata().get("user_id"));
            logger.info("Checkout session {} for user {} is paid and ready for fulfillment", sessionId, userId);

            var dbLog = app.getDatabase().prepareStatement(
                "INSERT INTO deposits_withdrawals (user_id, stripe_id, date, amount) VALUES (?, ?, NOW(), ?)"
            );
            dbLog.setInt(1, userId);
            dbLog.setString(2, sessionId);
            dbLog.setLong(3, checkoutSession.getAmountTotal() / 100);
            dbLog.executeUpdate();

            var dbBalance = app.getDatabase().prepareStatement("UPDATE users SET balance = balance + ? WHERE user_id = ?");
            dbBalance.setLong(1, checkoutSession.getAmountTotal() / 100);
            dbBalance.setInt(2, userId);
            dbBalance.executeUpdate();
        }
    }
}
