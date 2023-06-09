package cart.entity;

import cart.domain.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponType;
import java.math.BigDecimal;

public class CouponEntity {

    private final Long id;
    private final String name;
    private final String type;
    private final BigDecimal discountValue;
    private final BigDecimal minOrderPrice;

    public CouponEntity(String name, String type, BigDecimal discountValue, BigDecimal minOrderPrice) {
        this(null, name, type, discountValue, minOrderPrice);
    }

    public CouponEntity(Long id, String name, String type, BigDecimal discountValue, BigDecimal minOrderPrice) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.discountValue = discountValue;
        this.minOrderPrice = minOrderPrice;
    }

    public Coupon toDomain() {
        return new Coupon(id, name, CouponType.valueOf(type), discountValue, new Money(minOrderPrice));
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

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public BigDecimal getMinOrderPrice() {
        return minOrderPrice;
    }
}
