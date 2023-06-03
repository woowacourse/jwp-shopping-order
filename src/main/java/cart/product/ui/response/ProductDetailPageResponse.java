package cart.product.ui.response;

import java.util.List;

public class ProductDetailPageResponse {

    private final List<ProductDetailResponse> products;
    private final boolean isLast;

    public ProductDetailPageResponse(final List<ProductDetailResponse> products, final boolean isLast) {
        this.products = products;
        this.isLast = isLast;
    }

    public static ProductDetailPageResponse of(final List<ProductDetailResponse> products, final boolean isLast) {
        return new ProductDetailPageResponse(products, isLast);
    }

    public List<ProductDetailResponse> getProducts() {
        return products;
    }

    public boolean getIsLast() {
        return isLast;
    }
}
