package cart.controller.response;

import cart.domain.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponseDto {

    private Long id;
    private List<OrderProductResponseDto> orderProducts;
    private String timestamp;
    private Integer originPrice;
    private Integer discountPrice;
    private Integer totalPrice;

    public OrderResponseDto() {
    }

    public OrderResponseDto(final Long id,
                            final List<OrderProductResponseDto> orderProducts,
                            final String timestamp,
                            final Integer originPrice,
                            final Integer discountPrice,
                            final Integer totalPrice) {
        this.id = id;
        this.orderProducts = orderProducts;
        this.timestamp = timestamp;
        this.originPrice = originPrice;
        this.discountPrice = discountPrice;
        this.totalPrice = totalPrice;
    }

    public static OrderResponseDto from(final Order order) {
        Integer originalPrice = order.calculateTotalPrice();
        Integer cutPrice = order.calculateCutPrice();
        return new OrderResponseDto(
                order.getId(),
                toOrderProductResponseDtos(order),
                order.getTimeStamp().toString(),
                originalPrice,
                cutPrice,
                originalPrice - cutPrice
        );
    }

    private static List<OrderProductResponseDto> toOrderProductResponseDtos(final Order order) {
        return order.getOrderProducts().stream()
                .map(OrderProductResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<OrderProductResponseDto> getOrderProducts() {
        return orderProducts;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Long getId() {
        return id;
    }

    public Integer getOriginPrice() {
        return originPrice;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    @Override
    public String toString() {
        return "OrderResponseDto{" +
                "id=" + id +
                ", orderProducts=" + orderProducts +
                ", timestamp='" + timestamp + '\'' +
                ", originPrice=" + originPrice +
                ", discountPrice=" + discountPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }

}
