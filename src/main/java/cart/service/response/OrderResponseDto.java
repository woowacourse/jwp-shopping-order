package cart.service.response;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.domain.order.Order;
import java.util.List;

public class OrderResponseDto {

    private final Long id;
    private final List<OrderProductResponseDto> orderProducts;
    private final String timestamp;
    private final String couponName;
    private final Integer originPrice;
    private final Integer discountPrice;
    private final Integer totalPrice;

    private OrderResponseDto() {
        this(null, null, null, null, null, null, null);
    }

    public OrderResponseDto(final Long id, final List<OrderProductResponseDto> orderProducts,
                            final String timestamp,
                            final Integer originPrice, final String couponName, final Integer discountPrice,
                            final Integer totalPrice) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.timestamp = timestamp;
        this.originPrice = originPrice;
        this.couponName = couponName;
        this.discountPrice = discountPrice;
        this.totalPrice = totalPrice;
    }

    public static OrderResponseDto from(final Order order) {
        final List<OrderProductResponseDto> orderProductResponses = order.getOrderProducts().stream()
                .map(OrderProductResponseDto::from)
                .collect(toUnmodifiableList());
        final Builder responseBuilder = new Builder(
                order.getId()
                , orderProductResponses
                , order.getOrderAt().toString()
                , order.getOriginPrice().getValue()
                , order.getDiscountPrice().getValue()
                , order.getTotalPrice().getValue());
        order.getMemberCoupon().ifPresent(coupon -> responseBuilder.addCouponName(coupon.getCoupon().getName()));
        return responseBuilder.build();
    }

    public Long getId() {
        return id;
    }

    public List<OrderProductResponseDto> getOrderProducts() {
        return orderProducts;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getOriginPrice() {
        return originPrice;
    }

    public String getCouponName() {
        return couponName;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    private static class Builder {
        private final Long id;
        private final List<OrderProductResponseDto> orderProducts;
        private final String timestamp;
        private final Integer originPrice;
        private final Integer discountPrice;
        private final Integer totalPrice;
        private String couponName;

        private Builder(final Long id, final List<OrderProductResponseDto> orderProducts, final String timestamp,
                        final Integer originPrice,
                        final Integer discountPrice, final Integer totalPrice) {
            this.id = id;
            this.orderProducts = orderProducts;
            this.timestamp = timestamp;
            this.originPrice = originPrice;
            this.discountPrice = discountPrice;
            this.totalPrice = totalPrice;
        }

        public static Builder from(final Long id, final List<OrderProductResponseDto> orderProducts,
                                   final String timestamp,
                                   final Integer originPrice,
                                   final Integer discountPrice, final Integer totalPrice) {
            return new Builder(id, orderProducts, timestamp, originPrice, discountPrice, totalPrice);
        }

        public Builder addCouponName(final String couponName) {
            this.couponName = couponName;
            return this;
        }

        public OrderResponseDto build() {
            return new OrderResponseDto(id, orderProducts, timestamp, originPrice, couponName, discountPrice,
                    totalPrice);
        }
    }
}
