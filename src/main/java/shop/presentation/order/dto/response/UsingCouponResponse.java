package shop.presentation.order.dto.response;

public class UsingCouponResponse {
    private String name;
    private Integer discountRate;

    public UsingCouponResponse() {
    }

    public UsingCouponResponse(String name, Integer discountRate) {
        this.name = name;
        this.discountRate = discountRate;
    }

    public String getName() {
        return name;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }
}
