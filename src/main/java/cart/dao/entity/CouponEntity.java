package cart.dao.entity;

import java.util.Objects;

public class CouponEntity {

    private final Long id;
    private final String name;
    private final Integer minOrderPrice;
    private final Integer maxDiscountPrice;
    // TODO: 5/29/23 이거 이넘으로 바꿔야될 듯?
    private final String type;
    private final Integer discountAmount;
    private final Double discountPercentage;

    public CouponEntity(final Long id, final String name, final Integer minOrderPrice, final Integer maxDiscountPrice, final String type, final Integer discountAmount, final Double discountPercentage) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.maxDiscountPrice = maxDiscountPrice;
        this.type = type;
        this.discountAmount = discountAmount;
        this.discountPercentage = discountPercentage;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getMinOrderPrice() {
        return minOrderPrice;
    }

    public Integer getMaxDiscountPrice() {
        return maxDiscountPrice;
    }

    public String getType() {
        return type;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CouponEntity that = (CouponEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(minOrderPrice, that.minOrderPrice)
                && Objects.equals(maxDiscountPrice, that.maxDiscountPrice)
                && Objects.equals(type, that.type)
                && Objects.equals(discountAmount, that.discountAmount)
                && Objects.equals(discountPercentage, that.discountPercentage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, minOrderPrice, maxDiscountPrice, type, discountAmount, discountPercentage);
    }
}
