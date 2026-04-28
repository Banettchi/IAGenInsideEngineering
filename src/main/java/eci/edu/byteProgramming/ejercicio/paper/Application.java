package eci.edu.byteProgramming.ejercicio.paper;

import eci.edu.byteProgramming.ejercicio.paper.util.*;

/**
 * Entry point — demonstrates the Abstract Factory + Observer solution.
 *
 * Architecture recap
 * ──────────────────
 * Abstract Factory  →  PaymentFactory (interface)
 *                       ├─ CreditCardFactory
 *                       ├─ PaypalFactory
 *                       └─ CryptoFactory
 *
 * Observer          →  PaymentObserver (interface)
 *                       └─ PaymentEventObserver
 *                           ├─ Inventory   (discount stock)
 *                           ├─ Facturation (generate invoice)
 *                           └─ Notification (send email)
 *
 * The calling code (ECIPayment) only depends on the interfaces,
 * never on the concrete factories or observer implementations.
 */
public class Application {

    public static void main(String[] args) {

        // ── 1. Build the shared infrastructure ────────────────────────────
        Inventory    inventory    = new Inventory();
        Facturation  facturation  = new Facturation();
        Notification notification = new Notification();

        // ── 2. Register observer (Observer pattern) ────────────────────────
        ECIPayment eciPayment = new ECIPayment();
        eciPayment.addObserver(
            new PaymentEventObserver(inventory, facturation, notification)
        );

        // ── 3. Demo: Credit Card ───────────────────────────────────────────
        System.out.println("========================================");
        System.out.println("  DEMO 1: Credit Card");
        System.out.println("========================================");

        PaymentFactory creditCardFactory = new CreditCardFactory(
            "4111111111111111",   // Visa test number
            "Maria Garcia",
            "12/26",
            "123",
            "Calle 100 #45-30, Bogotá"
        );

        eciPayment.processPayment(
            creditCardFactory,
            150.00,
            "CUST-001",
            "Gaming Laptop purchase",
            "Maria Garcia",
            "maria@example.com",
            "LAPTOP001"
        );

        // ── 4. Demo: PayPal ────────────────────────────────────────────────
        System.out.println("========================================");
        System.out.println("  DEMO 2: PayPal");
        System.out.println("========================================");

        PaymentFactory paypalFactory = new PaypalFactory(
            "juan@paypal.com",
            "AUTH_TOKEN_DEMO_12345"
        );

        eciPayment.processPayment(
            paypalFactory,
            800.00,
            "CUST-002",
            "Smartphone purchase",
            "Juan Pérez",
            "juan@example.com",
            "PHONE001"
        );

        // ── 5. Demo: Cryptocurrency ────────────────────────────────────────
        System.out.println("========================================");
        System.out.println("  DEMO 3: Cryptocurrency");
        System.out.println("========================================");

        PaymentFactory cryptoFactory = new CryptoFactory(
            "1A2B3C4D5E6F7G8H9I0J1K2L3M4N5O6",   // 32-char wallet
            "BITCOIN",
            100.00   // wallet balance
        );

        eciPayment.processPayment(
            cryptoFactory,
            45.99,
            "CUST-003",
            "Java Programming Book",
            "Laura Martínez",
            "laura@example.com",
            "BOOK001"
        );

        System.out.println("========================================");
        System.out.println("  All demos completed.");
        System.out.println("========================================");
    }
}