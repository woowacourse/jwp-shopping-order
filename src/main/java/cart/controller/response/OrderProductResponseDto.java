package cart.controller.response;

import cart.domain.OrderProduct;
import cart.dto.ProductResponseDto;

public class OrderProductResponseDto {

    private ProductResponseDto productResponseDto;
    private Integer quantity;

    private OrderProductResponseDto() {
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

    public ProductResponseDto getProductResponseDto() {
        return productResponseDto;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
