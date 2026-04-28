package donmario.membership;

public class PremiumMembership implements MembershipStrategy {

    private static final int DISCOUNT_PERCENT = 20;

    @Override
    public String getName() { return "Premium"; }

    @Override
    public int applyDiscount(int subtotal) {
        return subtotal - (subtotal * DISCOUNT_PERCENT / 100);
    }

    @Override
    public int getDiscountPercent() { return DISCOUNT_PERCENT; }
}