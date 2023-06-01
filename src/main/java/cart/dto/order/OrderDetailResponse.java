package cart.dto.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderDetailResponse {

    private final Long orderId;
    private final Integer totalPrice;
    private final Integer usedPoint;
    private final Integer deliveryFee;
    private final LocalDateTime orderedAt;
    private final List<OrderProductDto> products;

    public OrderDetailResponse(final Long orderId, final Integer totalPrice, final Integer usedPoint, final Integer deliveryFee,
                               final LocalDateTime orderedAt, final List<OrderProductDto> orderProductDtos) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.deliveryFee = deliveryFee;
        this.orderedAt = orderedAt;
        this.products = orderProductDtos;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getUsedPoint() {
        return usedPoint;
    }

    public Integer getDeliveryFee() {
        return deliveryFee;
    }

    public String getOrderedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return orderedAt.format(formatter);
    }

    public List<OrderProductDto> getProducts() {
        return products;
    }
}
