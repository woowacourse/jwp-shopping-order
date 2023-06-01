package cart.service.response;

import cart.domain.order.Order;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDto {

    private final Long id;
    private final List<OrderProductResponseDto> orderProducts;
    private final LocalDateTime timestamp;
    private final String couponName;
    private final Integer originPrice;
    private final Integer discountPrice;
    private final Integer totalPrice;

    public OrderResponseDto(final Long id, final List<OrderProductResponseDto> orderProducts,
                            final LocalDateTime timestamp,
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
        final Long orderId = order.getId();
        return null;
    }

    public Long getId() {
        return id;
    }

    public List<OrderProductResponseDto> getOrderProducts() {
        return orderProducts;
    }

    public LocalDateTime getTimestamp() {
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
}
