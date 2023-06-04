package cart.dto;

import cart.domain.Coupon;
import cart.domain.CouponInfo;
import cart.domain.MemberCoupon;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderCouponResponse {
    private Long id;
    private String name;
    private Integer minOrderPrice;
    private Integer maxDiscountPrice;
    private Boolean isAvailable;
    private Integer discountPrice;
    private LocalDateTime expiredAt;

    public OrderCouponResponse() {
    }

    public OrderCouponResponse(final Long id, final String name, final Integer minOrderPrice, final Integer maxDiscountPrice, final Boolean isAvailable, final Integer discountPrice, final LocalDateTime expiredAt) {
        this.id = id;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.maxDiscountPrice = maxDiscountPrice;
        this.isAvailable = isAvailable;
        this.discountPrice = discountPrice;
        this.expiredAt = expiredAt;
    }

    public static OrderCouponResponse of(final MemberCoupon memberCoupon, final int totalPrice) {
        Coupon coupon = memberCoupon.getCoupon();
        CouponInfo couponInfo = coupon.getCouponInfo();
        if (coupon.isAvailable(totalPrice)) {
            return new OrderCouponResponse(
                    memberCoupon.getId(),
                    couponInfo.getName(),
                    couponInfo.getMinOrderPrice(),
                    couponInfo.getMaxDiscountPrice(),
                    true,
                    memberCoupon.calculateDiscount(totalPrice),
                    memberCoupon.getExpiredAt()
            );
        }
        return new OrderCouponResponse(
                couponInfo.getId(),
                couponInfo.getName(),
                couponInfo.getMinOrderPrice(),
                couponInfo.getMaxDiscountPrice(),
                false,
                null,
                memberCoupon.getExpiredAt()
        );
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

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderCouponResponse that = (OrderCouponResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(minOrderPrice, that.minOrderPrice)
                && Objects.equals(maxDiscountPrice, that.maxDiscountPrice)
                && Objects.equals(isAvailable, that.isAvailable)
                && Objects.equals(discountPrice, that.discountPrice)
                && Objects.equals(expiredAt, that.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, minOrderPrice, maxDiscountPrice, isAvailable, discountPrice, expiredAt);
    }

    @Override
    public String toString() {
        return "OrderCouponResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minOrderPrice=" + minOrderPrice +
                ", maxDiscountPrice=" + maxDiscountPrice +
                ", isAvailable=" + isAvailable +
                ", discountPrice=" + discountPrice +
                ", expiredAt=" + expiredAt +
                '}';
    }
}
