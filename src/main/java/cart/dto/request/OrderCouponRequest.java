package cart.dto.request;

import javax.validation.constraints.NotNull;

public class OrderCouponRequest {

    @NotNull(message = "쿠폰 아이디를 입력해주세요")
    private Long couponId;

    @NotNull(message = "쿠폰 명을 입력해주세요.")
    private String name;

    @NotNull(message = "할인 정보를 입력해주세요")
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
