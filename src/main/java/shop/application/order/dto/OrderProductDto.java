package shop.application.order.dto;

public class OrderProductDto {
    private Long productId;
    private int quantity;

    private OrderProductDto() {
    }


    public OrderProductDto(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
