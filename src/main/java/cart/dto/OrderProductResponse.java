package cart.dto;

import cart.domain.OrderDetail;

import java.util.List;
import java.util.stream.Collectors;

public class OrderProductResponse {

    private Long productId;
    private String productName;
    private int quantity;
    private int price;
    private String imageUrl;

    private OrderProductResponse(Long productId, String productName, int quantity, int price, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public static OrderProductResponse of(OrderDetail orderDetail) {
        return new OrderProductResponse(orderDetail.getProduct().getId(), orderDetail.getProductName(), orderDetail.getOrderQuantity(), orderDetail.getProductPrice(), orderDetail.getProductImageUrl());
    }

    public static List<OrderProductResponse> createOrderProductResponses(List<OrderDetail> orderDetails) {
        return orderDetails.stream().map(OrderProductResponse::of).collect(Collectors.toList());
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
