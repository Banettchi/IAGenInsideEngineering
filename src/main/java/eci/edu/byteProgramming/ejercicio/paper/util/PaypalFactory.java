package eci.edu.byteProgramming.ejercicio.paper.util;

/**
 * Concrete factory (Abstract Factory pattern) for PayPal payments.
 */
public class PaypalFactory implements PaymentFactory {

    private final String email;
    private final String authToken;

    public PaypalFactory(String email, String authToken) {
        this.email     = email;
        this.authToken = authToken;
    }

    @Override
    public PaymentMethod createPaymentMethod(double amount, String customerId, String description) {
        return new PaypalPayment(amount, customerId, description, email, authToken);
    }

    // ── Inner class ────────────────────────────────────────────────────────
    private static class PaypalPayment extends PaymentMethod {

        private final String email;
        private final String authToken;
        private String paypalTransactionId;

        PaypalPayment(double amount, String customerId, String description,
                      String email, String authToken) {
            super(amount, customerId, description);
            this.email     = email;
            this.authToken = authToken;
        }

        @Override
        public boolean validatePaymentMethod() {
            return validateEmail() && validateAuthToken();
        }

        private boolean validateEmail() {
            return email != null && email.contains("@") && email.contains(".");
        }

        private boolean validateAuthToken() {
            return authToken != null && authToken.length() > 10;
        }

        @Override
        public boolean processPayment() {
            System.out.println("Processing PayPal payment...");

            if (!validatePaymentMethod()) {
                System.out.println("PayPal validation failed!");
                setStatus(PaymentStatus.FAILED);
                return false;
            }

            setStatus(PaymentStatus.PROCESSING);
            try {
                Thread.sleep(1500);
                this.paypalTransactionId = "PP" + System.currentTimeMillis();
                System.out.println("PayPal payment authorized for: " + email);
                setStatus(PaymentStatus.COMPLETED);
                return true;
            } catch (Exception e) {
                setStatus(PaymentStatus.FAILED);
                return false;
            }
        }

        @Override
        public String getPaymentMethod() { return "PAYPAL"; }
    }
}