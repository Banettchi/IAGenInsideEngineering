package eci.edu.byteProgramming.ejercicio.paper.util;

/**
 * Concrete factory (Abstract Factory pattern) that produces a
 * credit-card payment method together with its own validation logic.
 *
 * The factory stores the card credentials and injects them when
 * createPaymentMethod() is called, so ECIPayment never touches card data.
 */
public class CreditCardFactory implements PaymentFactory {

    private final String number;
    private final String name;
    private final String expirationDate;
    private final String cvv;
    private final String address;

    public CreditCardFactory(String number, String name,
                             String expirationDate, String cvv, String address) {
        this.number         = number;
        this.name           = name;
        this.expirationDate = expirationDate;
        this.cvv            = cvv;
        this.address        = address;
    }

    @Override
    public PaymentMethod createPaymentMethod(double amount, String customerId, String description) {
        return new CreditCardPayment(amount, customerId, description,
                                     number, name, expirationDate, cvv, address);
    }

    // ── Inner class: the actual PaymentMethod ──────────────────────────────
    private static class CreditCardPayment extends PaymentMethod {

        private final String number;
        private final String name;
        private final String expirationDate;
        private final String cvv;
        private final String cardType;

        CreditCardPayment(double amount, String customerId, String description,
                          String number, String name,
                          String expirationDate, String cvv, String address) {
            super(amount, customerId, description);
            this.number         = number;
            this.name           = name;
            this.expirationDate = expirationDate;
            this.cvv            = cvv;
            this.cardType       = determineCardType(number);
        }

        @Override
        public boolean validatePaymentMethod() {
            return validateCardNumber() && validateCVV() && validateExpirationDate();
        }

        private boolean validateCardNumber() {
            return number != null && number.length() >= 13 && number.length() <= 19;
        }

        private boolean validateCVV() {
            return cvv != null && cvv.length() >= 3 && cvv.length() <= 4;
        }

        private boolean validateExpirationDate() {
            return expirationDate != null && expirationDate.matches("\\d{2}/\\d{2}");
        }

        @Override
        public boolean processPayment() {
            System.out.println("Processing Credit Card payment...");

            if (!validatePaymentMethod()) {
                System.out.println("Credit Card validation failed!");
                setStatus(PaymentStatus.FAILED);
                return false;
            }

            setStatus(PaymentStatus.PROCESSING);
            try {
                Thread.sleep(2000);
                System.out.println("Contacting bank for card: " + maskCardNumber());
                System.out.println("Payment authorized by bank");
                setStatus(PaymentStatus.COMPLETED);
                return true;
            } catch (Exception e) {
                setStatus(PaymentStatus.FAILED);
                return false;
            }
        }

        @Override
        public String getPaymentMethod() { return "CREDIT_CARD"; }

        private String determineCardType(String cardNumber) {
            if (cardNumber.startsWith("4")) return "VISA";
            if (cardNumber.startsWith("5")) return "MASTERCARD";
            if (cardNumber.startsWith("3")) return "AMEX";
            return "UNKNOWN";
        }

        public String maskCardNumber() {
            return "**** **** **** " + number.substring(number.length() - 4);
        }
    }
}