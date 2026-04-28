package eci.edu.byteProgramming.ejercicio.paper.util;

/**
 * Concrete factory (Abstract Factory pattern) for Cryptocurrency payments.
 *
 * FIX: Original code had "this.token = token" where token was never passed
 * in the constructor (null reference). Removed the unused token field.
 */
public class CryptoFactory implements PaymentFactory {

    private final String walletAddress;
    private final String cryptoType;
    private final double walletBalance;

    public CryptoFactory(String walletAddress, String cryptoType, double walletBalance) {
        this.walletAddress = walletAddress;
        this.cryptoType    = cryptoType;
        this.walletBalance = walletBalance;
    }

    @Override
    public PaymentMethod createPaymentMethod(double amount, String customerId, String description) {
        return new CryptoPayment(amount, customerId, description,
                                  walletAddress, cryptoType, walletBalance);
    }

    // ── Inner class ────────────────────────────────────────────────────────
    private static class CryptoPayment extends PaymentMethod {

        private final String walletAddress;
        private final String cryptoType;
        private final double walletBalance;
        private String blockchainHash;

        CryptoPayment(double amount, String customerId, String description,
                      String walletAddress, String cryptoType, double walletBalance) {
            super(amount, customerId, description);
            this.walletAddress = walletAddress;
            this.cryptoType    = cryptoType;
            this.walletBalance = walletBalance;
            // FIX: removed "this.token = token" — token was never passed and was always null
        }

        @Override
        public boolean validatePaymentMethod() {
            return validateWalletAddress() && validateBalance();
        }

        private boolean validateWalletAddress() {
            return walletAddress != null && walletAddress.length() >= 26;
        }

        private boolean validateBalance() {
            return walletBalance >= amount;
        }

        @Override
        public boolean processPayment() {
            System.out.println("Processing Cryptocurrency payment...");

            if (!validatePaymentMethod()) {
                System.out.println("Crypto validation failed!");
                setStatus(PaymentStatus.FAILED);
                return false;
            }

            setStatus(PaymentStatus.PROCESSING);
            try {
                Thread.sleep(3000);
                this.blockchainHash = generateBlockchainHash();
                System.out.println("Transaction broadcasted to blockchain");
                System.out.println("Blockchain hash: " + blockchainHash);
                setStatus(PaymentStatus.COMPLETED);
                return true;
            } catch (Exception e) {
                setStatus(PaymentStatus.FAILED);
                return false;
            }
        }

        @Override
        public String getPaymentMethod() { return "CRYPTOCURRENCY"; }

        private String generateBlockchainHash() {
            return "0x" + Integer.toHexString((int) (Math.random() * Integer.MAX_VALUE));
        }
    }
}