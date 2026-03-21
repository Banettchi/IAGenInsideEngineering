package eci.edu.dosw.parcial.parcial1cvdsdosw01.util;

public interface PaymentObserver {
    void onPaymentSuccess(PaymentMethod payment, String customerName, String customerEmail, String productId);
    void onPaymentFailed(PaymentMethod payment, String customerEmail);
}
