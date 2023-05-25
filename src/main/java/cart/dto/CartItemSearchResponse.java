package cart.dto;

import java.util.List;

public class CartItemSearchResponse {

    private final List<ProductDto> products;

    public CartItemSearchResponse(final List<ProductDto> products) {
        this.products = products;
    }

    public List<ProductDto> getProducts() {
        return products;
    }
}
