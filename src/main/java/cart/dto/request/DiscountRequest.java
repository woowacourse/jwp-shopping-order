package cart.dto.request;

public class DiscountRequest {

    private String type;
    private Integer amount;

    public DiscountRequest() {
    }

    public DiscountRequest(String type, Integer amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public Integer getAmount() {
        return amount;
    }
}
