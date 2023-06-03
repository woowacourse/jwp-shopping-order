package cart.dto.request;

public class CouponRequest {

    private final String name;
    private final String type;
    private final int amount;

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
