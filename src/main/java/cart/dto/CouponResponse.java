package cart.dto;

import cart.domain.Coupon;

import java.util.Objects;

public class CouponResponse {

    private final Long id;
    private final String name;
    private final int discountPrice;

    private CouponResponse(Long id, String name, int discountPrice) {
        this.id = id;
        this.name = name;
        this.discountPrice = discountPrice;
    }

    public static CouponResponse of(Coupon coupon) {
        return new CouponResponse(coupon.getId(), coupon.getName(), coupon.getDiscountValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CouponResponse that = (CouponResponse) o;
        return discountPrice == that.discountPrice && Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, discountPrice);
    }

    @Override
    public String toString() {
        return "CouponResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", discountPrice=" + discountPrice +
                '}';
    }
}
