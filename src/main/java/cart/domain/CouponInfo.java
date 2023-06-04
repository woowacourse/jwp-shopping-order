package cart.domain;

import java.util.Objects;

public class CouponInfo {
    private final Long id;
    private final String name;
    private final Integer minOrderPrice;
    private final Integer maxDiscountPrice;

    public CouponInfo(final Long id, final String name, final Integer minOrderPrice, final Integer maxDiscountPrice) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.maxDiscountPrice = maxDiscountPrice;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CouponInfo that = (CouponInfo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CouponInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minPrice=" + minOrderPrice +
                ", maxPrice=" + maxDiscountPrice +
                '}';
    }
}
