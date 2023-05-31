package cart.dto;

import cart.domain.product.Product;

import java.util.List;

public class HomePagingResponse {

    private final List<Product> products;
    private final boolean isLast;

    private HomePagingResponse(final List<Product> products, final boolean isLast) {
        this.products = products;
        this.isLast = isLast;
    }

    public static HomePagingResponse of(final List<Product> products, final boolean isLast) {
        return new HomePagingResponse(products, isLast);
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean getIsLast() {
        return isLast;
    }
}
