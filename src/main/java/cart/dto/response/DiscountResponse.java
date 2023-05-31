package cart.dto.response;

public class DiscountResponse {

    private String type;
    private Integer amount;

    public DiscountResponse() {
    }

    public DiscountResponse(String type, Integer amount) {
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
