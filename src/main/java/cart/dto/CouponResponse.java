package cart.dto;

import cart.domain.Coupon;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CouponResponse {

    @JsonProperty("couponId")
    private Long id;
    private String name;
    @JsonProperty("discount")
    private DiscountDto discount;

    private CouponResponse(final Long id, final String name, final DiscountDto discount) {
        this.id = id;
        this.name = name;
        this.discount = discount;
    }

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getName(), new DiscountDto(coupon.getDiscount()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DiscountDto getDiscount() {
        return discount;
    }
}
