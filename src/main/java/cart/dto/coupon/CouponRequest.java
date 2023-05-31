package cart.dto.coupon;

public class CouponRequest {

    private String name;
    private String type;
    private int amount;

    private CouponRequest() {
    }

    public CouponRequest(final String name, final String type, final int amount) {
        this.name = name;
        this.type = type;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }
}
