package cart.dto;

import java.util.List;

public class HomePagingResponse {

    private final List<ProductCartItemResponse> products;
    private final boolean isLast;

    public HomePagingResponse(final List<ProductCartItemResponse> products, final boolean isLast) {
        this.products = products;
        this.isLast = isLast;
    }

    public static HomePagingResponse of(final List<ProductCartItemResponse> products, final boolean isLast) {
        return new HomePagingResponse(products, isLast);
    }

    public List<ProductCartItemResponse> getProducts() {
        return products;
    }

    public boolean getIsLast() {
        return isLast;
    }
}
