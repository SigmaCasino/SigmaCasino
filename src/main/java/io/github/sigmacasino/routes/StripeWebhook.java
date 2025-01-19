package io.github.sigmacasino.routes;

import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionRetrieveParams;
import io.github.sigmacasino.App;
import io.github.sigmacasino.PostRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

public class StripeWebhook extends PostRoute {
    private String webhookSecret = System.getenv("STRIPE_WEBHOOK_SECRET");
    private Logger logger = LoggerFactory.getLogger(getClass());

    public StripeWebhook(App app) {
        super(app, "/webhook/stripe");
    }

    @Override
    public void handlePost(Request request, Response response) {
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

    public void fulfillCheckout(String sessionId) throws StripeException {
        logger.debug("Fulfilling checkout session {}", sessionId);

        // Retrieve the Checkout Session from the API with line_items expanded
        SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("line_items").build();

        Session checkoutSession = Session.retrieve(sessionId, params, null);

        // Check the Checkout Session's payment_status property to determine if fulfillment should be peformed
        if (!"unpaid".equals(checkoutSession.getPaymentStatus())) {
            // TODO: Perform fulfillment of the line items
            logger.info("Checkout session {} is paid and ready for fulfillment", sessionId);
        }
    }
}
