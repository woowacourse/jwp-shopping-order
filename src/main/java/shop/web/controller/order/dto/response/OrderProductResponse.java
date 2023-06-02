package shop.web.controller.order.dto.response;

public class OrderProductResponse {
    OrderProductDetailResponse product;
    private Integer quantity;

    private OrderProductResponse() {
    }

    public OrderProductResponse(OrderProductDetailResponse product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public OrderProductDetailResponse getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
