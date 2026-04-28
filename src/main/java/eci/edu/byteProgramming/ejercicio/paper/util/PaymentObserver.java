package eci.edu.byteProgramming.ejercicio.paper.util;

/**
 * Concrete Observer (Observer pattern) — reacts to payment events and
 * coordinates the three post-payment modules: Inventory, Facturation,
 * and Notification.
 *
 * FIX: original file imported javax.management.Notification instead of
 * the project's own Notification class, causing a compile error because
 * javax.management.Notification has no sendConfirmationEmail() method.
 */
public class PaymentEventObserver implements PaymentObserver {

    private final Inventory    inventory;
    private final Facturation  facturation;
    private final Notification notification;   // FIX: project class, not javax.management

    public PaymentEventObserver(Inventory inventory,
                                Facturation facturation,
                                Notification notification) {
        this.inventory    = inventory;
        this.facturation  = facturation;
        this.notification = notification;
    }

    @Override
    public void onPaymentSuccess(PaymentMethod payment, String customerName,
                                 String customerEmail, String productId) {

        System.out.println("\nPayment Observer: Processing successful payment events...");

        // 1. Inventory module: discount stock
        Product product = inventory.getProduct(productId);
        if (product != null) {
            inventory.discountProduct(productId, 1);
        }

        // 2. Facturation module: generate invoice
        String productDetails = product != null ? product.getName() : "Product";
        facturation.generateInvoice(payment, customerName, productDetails);

        // 3. Notification module: send confirmation email
        notification.sendConfirmationEmail(customerEmail, customerName, payment);

        System.out.println("All post-payment processes completed successfully!\n");
    }

    @Override
    public void onPaymentFailed(PaymentMethod payment, String customerEmail) {
        System.out.println("\nPayment Observer: Processing failed payment events...");
        notification.sendFailureNotification(payment, customerEmail);
        System.out.println("Failed payment processes completed.\n");
    }
}