package cart.dto;

public class CouponCreateRequest {

    private final String type;
    private final int amount;
    private final String name;

    public CouponCreateRequest(String type, int amount, String name) {
        this.type = type;
        this.amount = amount;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }
}
