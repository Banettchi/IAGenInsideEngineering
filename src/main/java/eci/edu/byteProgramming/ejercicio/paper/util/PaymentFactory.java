package eci.edu.byteProgramming.ejercicio.paper.util;

/**
 * Abstract Factory interface — defines the contract for creating
 * families of related payment objects (payment method + its validator).
 *
 * Pattern: Abstract Factory
 * Each concrete factory produces a PaymentMethod already wired with
 * its own validation logic, so the calling code (ECIPayment) never
 * needs to know which method is being used.
 */
public interface PaymentFactory {

    /**
     * Creates a fully configured PaymentMethod for the given transaction.
     *
     * @param amount      amount to charge
     * @param customerId  customer identifier
     * @param description human-readable description of the purchase
     * @return a ready-to-use PaymentMethod (credit card / PayPal / crypto)
     */
    PaymentMethod createPaymentMethod(double amount, String customerId, String description);
}