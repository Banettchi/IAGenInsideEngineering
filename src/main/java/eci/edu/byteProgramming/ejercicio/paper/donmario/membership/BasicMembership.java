package donmario.membership;

public class BasicMembership implements MembershipStrategy {

    @Override
    public String getName() { return "Basica"; }

    @Override
    public int applyDiscount(int subtotal) { return subtotal; }

    @Override
    public int getDiscountPercent() { return 0; }
}