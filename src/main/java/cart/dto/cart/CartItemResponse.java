package cart.dto.cart;

import cart.dto.product.ProductResponse;

public class CartItemResponse {
    private Long id;
    private int quantity;
    private ProductResponse product;

    public CartItemResponse(Long id, int quantity, ProductResponse product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
