package cart.service.response;

import cart.domain.order.OrderProduct;

public class OrderProductResponseDto {

    private final ProductResponse productResponse;
    private final Integer quantity;

    private OrderProductResponseDto() {
        this(null, null);
    }

    public OrderProductResponseDto(final ProductResponse productResponse, final Integer quantity) {
        this.productResponse = productResponse;
        this.quantity = quantity;
    }

    public static OrderProductResponseDto from(final OrderProduct orderProduct) {
        final ProductResponse productResponse = ProductResponse.from(orderProduct.getProduct());
        return new OrderProductResponseDto(productResponse, orderProduct.getQuantity().getValue());
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
