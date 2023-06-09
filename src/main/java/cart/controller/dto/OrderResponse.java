package cart.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class OrderResponse {

    @Schema(description = "주문 아이디", example = "1")
    private final Long id;
    @Schema(description = "주문 상품 목록")
    private final List<OrderItemResponse> orderItems;
    @Schema(description = "날짜", example = "2023-01-01T12:12:12")
    private final Date date;
    @Schema(description = "가격", example = "10000")
    private final int price;

    public OrderResponse(final Long id, final List<OrderItemResponse> orderItems, final Date date, final int price) {
        this.id = id;
        this.orderItems = orderItems;
        this.date = date;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public Date getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }
}
