package cart.persistence.dao.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OrderDto {

    private final Long orderId;
    private final LocalDateTime orderedAt;
    private final Integer orderQuantity;
    private final Integer totalPrice;
    private final Integer discountedTotalPrice;
    private final Integer deliveryPrice;
    private final Long couponId;
    private final String couponName;
    private final Integer couponDiscountRate;
    private final Integer couponPeriod;
    private final Timestamp couponExpiredAt;
    private final Long memberId;
    private final String memberName;
    private final String memberPassword;
    private final Long productId;
    private final String productName;
    private final Integer productPrice;
    private final String productImageUrl;
    private final Boolean productIsDeleted;

    public OrderDto(final Long orderId, final LocalDateTime orderedAt, final Integer orderQuantity,
                    final Integer totalPrice, final Integer discountedTotalPrice, final Integer deliveryPrice,
                    final Long couponId, final String couponName, final Integer couponDiscountRate,
                    final Integer couponPeriod, final Timestamp couponExpiredAt,
                    final Long memberId, final String memberName, final String memberPassword, final Long productId,
                    final String productName, final Integer productPrice,
                    final String productImageUrl, final Boolean productIsDeleted) {
        this.orderId = orderId;
        this.orderedAt = orderedAt;
        this.orderQuantity = orderQuantity;
        this.totalPrice = totalPrice;
        this.discountedTotalPrice = discountedTotalPrice;
        this.deliveryPrice = deliveryPrice;
        this.couponId = couponId;
        this.couponName = couponName;
        this.couponDiscountRate = couponDiscountRate;
        this.couponPeriod = couponPeriod;
        this.couponExpiredAt = couponExpiredAt;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
        this.productIsDeleted = productIsDeleted;
    }

    public Long getOrderId() {
        return orderId;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getDiscountedTotalPrice() {
        return discountedTotalPrice;
    }

    public Integer getDeliveryPrice() {
        return deliveryPrice;
    }

    public Long getCouponId() {
        return couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getCouponDiscountRate() {
        return couponDiscountRate;
    }

    public Integer getCouponPeriod() {
        return couponPeriod;
    }

    public LocalDateTime getCouponExpiredAt() {
        if (couponExpiredAt != null) {
            return couponExpiredAt.toLocalDateTime();
        }
        return null;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberPassword() {
        return memberPassword;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public Boolean getProductIsDeleted() {
        return productIsDeleted;
    }
}
