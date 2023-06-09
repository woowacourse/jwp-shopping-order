package cart.dao.entity;

import cart.domain.CouponType;
import cart.domain.DiscountType;
import cart.domain.Money;

public class CouponTypeEntity {

    private final long id;
    private final String name;
    private final String discountType;
    private final long discountAmount;

    public CouponTypeEntity(final long id, final String name, final String discountType, final long discountAmount) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
    }

    public CouponType create() {
        return new CouponType(id, name, DiscountType.find(discountType), new Money(discountAmount));
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDiscountType() {
        return discountType;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }
}
