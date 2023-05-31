package cart.entity;

import cart.domain.Coupon;
import cart.domain.CouponType;

public class CouponEntity {

    private Long id;
    private String name;
    private String type;
    private Integer discountAmount;

    public CouponEntity(Long id, String name, String type, Integer discountAmount) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.discountAmount = discountAmount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Coupon toDomain() {
        return new Coupon(id, name, CouponType.from(type), discountAmount);
    }
}
