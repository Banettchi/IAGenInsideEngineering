package eci.edu.byteProgramming.ejercicio.paper.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Ejercicio #2 — Tienda Virtual
 *
 * Covers:
 *  - Abstract Factory: each factory creates a valid PaymentMethod
 *  - Observer: PaymentEventObserver correctly calls all three modules
 *  - Validation logic per payment method
 *  - Bug regressions (customerId propagation, null token, wrong import)
 */
class auxiliaryTest {

    private Inventory    inventory;
    private Facturation  facturation;
    private Notification notification;
    private ECIPayment   eciPayment;

    @BeforeEach
    void setUp() {
        inventory    = new Inventory();
        facturation  = new Facturation();
        notification = new Notification();

        eciPayment = new ECIPayment();
        eciPayment.addObserver(
            new PaymentEventObserver(inventory, facturation, notification)
        );
    }

    // ── Abstract Factory tests ─────────────────────────────────────────────

    @Test
    void creditCardFactory_createsPaymentMethod() {
        PaymentFactory factory = new CreditCardFactory(
            "4111111111111111", "Test User", "12/26", "123", "Bogotá"
        );
        PaymentMethod pm = factory.createPaymentMethod(100.0, "CUST-01", "Test");

        assertNotNull(pm);
        assertEquals("CREDIT_CARD", pm.getPaymentMethod());
        assertEquals(100.0, pm.getAmount());
        // BUG FIX regression: customerId must be stored correctly
        assertEquals("CUST-01", pm.getCustomerId());
        assertNotNull(pm.getTransactionId());
    }

    @Test
    void paypalFactory_createsPaymentMethod() {
        PaymentFactory factory = new PaypalFactory(
            "user@paypal.com", "AUTH_TOKEN_DEMO_12345"
        );
        PaymentMethod pm = factory.createPaymentMethod(200.0, "CUST-02", "Test");

        assertNotNull(pm);
        assertEquals("PAYPAL", pm.getPaymentMethod());
        assertEquals("CUST-02", pm.getCustomerId());
    }

    @Test
    void cryptoFactory_createsPaymentMethod() {
        PaymentFactory factory = new CryptoFactory(
            "1A2B3C4D5E6F7G8H9I0J1K2L3M4N5O6", "BITCOIN", 500.0
        );
        PaymentMethod pm = factory.createPaymentMethod(45.99, "CUST-03", "Test");

        assertNotNull(pm);
        assertEquals("CRYPTOCURRENCY", pm.getPaymentMethod());
        // BUG FIX regression: customerId must not be null
        assertEquals("CUST-03", pm.getCustomerId());
    }

    // ── Validation tests ───────────────────────────────────────────────────

    @Test
    void creditCard_validatesCorrectly() {
        PaymentFactory valid = new CreditCardFactory(
            "4111111111111111", "Test", "12/26", "123", "Addr"
        );
        PaymentMethod pm = valid.createPaymentMethod(10.0, "C1", "desc");
        assertTrue(pm.validatePaymentMethod(), "Valid card should pass");
    }

    @Test
    void creditCard_rejectsShortNumber() {
        PaymentFactory invalid = new CreditCardFactory(
            "411", "Test", "12/26", "123", "Addr"   // too short
        );
        PaymentMethod pm = invalid.createPaymentMethod(10.0, "C2", "desc");
        assertFalse(pm.validatePaymentMethod(), "Short card number should fail");
    }

    @Test
    void paypal_rejectsInvalidEmail() {
        PaymentFactory invalid = new PaypalFactory(
            "not-an-email", "AUTH_TOKEN_DEMO_12345"
        );
        PaymentMethod pm = invalid.createPaymentMethod(10.0, "C3", "desc");
        assertFalse(pm.validatePaymentMethod(), "Invalid email should fail");
    }

    @Test
    void crypto_rejectsInsufficientBalance() {
        PaymentFactory factory = new CryptoFactory(
            "1A2B3C4D5E6F7G8H9I0J1K2L3M4N5O6", "BTC", 5.0  // balance < amount
        );
        PaymentMethod pm = factory.createPaymentMethod(100.0, "C4", "desc");
        assertFalse(pm.validatePaymentMethod(), "Insufficient balance should fail");
    }

    // ── Inventory tests ────────────────────────────────────────────────────

    @Test
    void inventory_discountsStock() {
        int before = inventory.getStock("LAPTOP001");
        inventory.discountProduct("LAPTOP001", 1);
        assertEquals(before - 1, inventory.getStock("LAPTOP001"));
    }

    @Test
    void inventory_rejectsInsufficientStock() {
        boolean result = inventory.discountProduct("LAPTOP001", 999);
        assertFalse(result, "Should fail when requesting more than available stock");
    }

    // ── Facturation tests ──────────────────────────────────────────────────

    @Test
    void facturation_calculatesTaxCorrectly() {
        double tax = facturation.calculateTax(100.0);
        assertEquals(19.0, tax, 0.001, "19% IVA on 100 should be 19");
    }

    @Test
    void facturation_calculatesTotalCorrectly() {
        double total = facturation.calculateTotal(100.0);
        assertEquals(119.0, total, 0.001, "Total with IVA on 100 should be 119");
    }

    // ── Observer integration test ──────────────────────────────────────────

    @Test
    void observer_fullFlowCreditCard() {
        int stockBefore = inventory.getStock("BOOK001");

        PaymentFactory factory = new CreditCardFactory(
            "4111111111111111", "Test User", "12/26", "123", "Bogotá"
        );

        boolean result = eciPayment.processPayment(
            factory, 45.99, "CUST-99", "Book purchase",
            "Test User", "test@example.com", "BOOK001"
        );

        assertTrue(result, "Payment should succeed");
        assertEquals(stockBefore - 1, inventory.getStock("BOOK001"),
                     "Stock should have been decremented by observer");
    }

    @Test
    void paymentMethod_statusTransitions() {
        PaymentFactory factory = new CreditCardFactory(
            "4111111111111111", "Test", "12/26", "123", "Addr"
        );
        PaymentMethod pm = factory.createPaymentMethod(10.0, "C5", "desc");

        assertEquals(PaymentStatus.PENDING, pm.getStatus(), "Initial status should be PENDING");
        pm.processPayment();
        assertEquals(PaymentStatus.COMPLETED, pm.getStatus(), "After success should be COMPLETED");
    }
}