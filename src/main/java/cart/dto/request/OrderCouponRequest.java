package cart.dto.request;

public class OrderCouponRequest {

    private Long couponId;
    private String name;
    private DiscountRequest discount;

    public OrderCouponRequest() {
    }

    public OrderCouponRequest(Long couponId, String name, DiscountRequest discount) {
        this.couponId = couponId;
        this.name = name;
        this.discount = discount;
    }

    public Long getCouponId() {
        return couponId;
    }

    public String getName() {
        return name;
    }

    public DiscountRequest getDiscount() {
        return discount;
    }
}
