package cart.domain.order.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OrderedProductDto {

    private final OrderItemDto product;
    private final int quantity;

    public OrderedProductDto(final OrderItemDto product, final int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public OrderItemDto getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    @JsonIgnore
    public Long getProductId() {
        return product.getId();
    }
}
