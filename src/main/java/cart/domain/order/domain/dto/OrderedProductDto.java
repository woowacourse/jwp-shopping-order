package cart.domain.order.domain.dto;

import cart.domain.product.application.dto.ProductResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class OrderedProductDto {

    private final ProductResponse product;
    private final int quantity;

    public OrderedProductDto(final ProductResponse product, final int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductResponse getProduct() {
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
