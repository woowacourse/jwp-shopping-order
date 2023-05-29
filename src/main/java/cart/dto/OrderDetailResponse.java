package cart.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderDetailResponse {

    private final Long orderId;
    private final Integer totalPrice;
    private final Integer usedPoint;
    private final LocalDateTime createdAt;
    private final List<OrderProductDto> products;

    public OrderDetailResponse(final Long orderId, final Integer totalPrice, final Integer usedPoint,
                               final LocalDateTime createdAt, final List<OrderProductDto> orderProductDtos) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.createdAt = createdAt;
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

    public String getCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return createdAt.format(formatter);
    }

    public List<OrderProductDto> getProducts() {
        return products;
    }
}
