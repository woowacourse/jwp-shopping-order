package cart.dto;

import cart.dao.CouponTypeDao;
import cart.domain.CouponType;

public class CouponTypeResponse {

    private final Long id;
    private final String name;
    private final int discountAmount;
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

    public int getDiscountAmount() {
        return discountAmount;
    }

    public String getDescription() {
        return description;
    }

}
