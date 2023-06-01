package cart.dao.dto;

import cart.domain.Coupon;
import cart.domain.CouponType;

public class CouponDto {

    private final Long id;
    private final String name;
    private final Integer value;
    private final String couponType;

    public CouponDto(final Long id, final String name, final Integer value, final String couponType) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.couponType = couponType;
    }

    public CouponDto(final String name, final Integer value, final String couponType) {
        this(null, name, value, couponType);
    }

    public static CouponDto from(final Coupon coupon) {
        return new CouponDto(coupon.getId(), coupon.getName(), coupon.getValue(), coupon.getCouponType().toString());
    }

    public Coupon toCoupon() {
        return new Coupon(id, name, value, CouponType.from(couponType));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public String getCouponType() {
        return couponType;
    }
}
