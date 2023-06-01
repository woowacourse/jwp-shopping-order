package cart.controller.response;

import cart.domain.OrderProduct;
import cart.domain.Quantity;
import cart.dto.ProductResponseDto;

public class OrderProductResponseDto {

    private final ProductResponseDto productResponseDto;
    private final Integer quantity;

    private OrderProductResponseDto() {
        this(null, null);
    }

    public OrderProductResponseDto(final ProductResponseDto productResponseDto, final Integer quantity) {
        this.productResponseDto = productResponseDto;
        this.quantity = quantity;
    }

    public static OrderProductResponseDto from(final OrderProduct orderProduct) {
        return new OrderProductResponseDto(
                ProductResponseDto.from(orderProduct.getProduct()),
                orderProduct.getQuantity().getValue()
        );
    }

    public ProductResponseDto getProductResponse() {
        return productResponseDto;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
