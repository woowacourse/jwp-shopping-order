package cart.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import cart.domain.Order;

public class OrderDetailResponse {
    private final Long id;
    private final List<OrderItemResponse> products;
    private final int totalProductPrice;
    private final int discountPrice;
    private final int shippingFee;

    private OrderDetailResponse(Long id, List<OrderItemResponse> products, int totalProductPrice, int discountPrice,
        int shippingFee) {
        this.id = id;
        this.products = products;
        this.totalProductPrice = totalProductPrice;
        this.discountPrice = discountPrice;
        this.shippingFee = shippingFee;
    }

    public static OrderDetailResponse of(Order order) {
        return new OrderDetailResponse(
            order.getId(),
            order.getOrderItems().stream().map(OrderItemResponse::of).collect(Collectors.toUnmodifiableList()),
            order.getTotalPrice().value(),
            order.getDiscountPrice().value(),
            order.getShippingFee().value()
        );
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemResponse> getProducts() {
        return products;
    }

    public int getTotalProductPrice() {
        return totalProductPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public int getShippingFee() {
        return shippingFee;
    }
}
