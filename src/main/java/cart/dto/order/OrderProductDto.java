package cart.dto.order;

import cart.dto.product.ProductResponse;

public class OrderProductDto {

    private final Integer quantity;
    private final ProductResponse product;

    public OrderProductDto(final Long id, final String name, final Integer price, final String imageUrl, final Integer quantity) {
        this.quantity = quantity;
        this.product = new ProductResponse(id, name, price, imageUrl);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
