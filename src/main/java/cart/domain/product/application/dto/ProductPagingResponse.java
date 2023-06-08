package cart.domain.product.application.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductPagingResponse {

    private final List<ProductCartItemResponse> products;

    private final Boolean last;

    public ProductPagingResponse(List<ProductCartItemResponse> products, Boolean last) {
        this.products = new ArrayList<>(products);
        this.last = last;
    }

    public List<ProductCartItemResponse> getProducts() {
        return List.copyOf(products);
    }

    public Boolean getLast() {
        return last;
    }
}
