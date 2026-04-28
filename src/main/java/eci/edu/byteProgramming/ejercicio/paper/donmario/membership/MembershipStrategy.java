package donmario.membership;

public interface MembershipStrategy {
    String getName();
    int applyDiscount(int subtotal);
    int getDiscountPercent();
}