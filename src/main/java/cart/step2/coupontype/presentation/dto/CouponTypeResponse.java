package cart.step2.coupontype.presentation.dto;

import cart.step2.coupontype.domain.CouponType;

public class CouponTypeResponse {

    private final Long id;
    private final String name;
    private final Integer discountAmount;
    private final String description;

    public CouponTypeResponse(final CouponType couponType) {
        this.id = couponType.getId();
        this.name = couponType.getName();
        this.discountAmount = couponType.getDiscountAmount();
        this.description = couponType.getDescription();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public String getDescription() {
        return description;
    }

}
