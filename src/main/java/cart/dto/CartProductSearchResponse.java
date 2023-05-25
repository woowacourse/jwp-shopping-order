package cart.dto;

import java.util.List;

public class CartProductSearchResponse {

    private final List<ProductDto> products;

    public CartProductSearchResponse(final List<ProductDto> products) {
        this.products = products;
    }

    public List<ProductDto> getProducts() {
        return products;
    }
}
