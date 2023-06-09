package cart.product.ui.response;

import java.util.List;

public class ProductDetailPageResponse {

    private final List<ProductDetailResponse> products;
    private final boolean last;

    public ProductDetailPageResponse(final List<ProductDetailResponse> products, final boolean last) {
        this.products = products;
        this.last = last;
    }

    public static ProductDetailPageResponse of(final List<ProductDetailResponse> products, final boolean isLast) {
        return new ProductDetailPageResponse(products, isLast);
    }

    public List<ProductDetailResponse> getProducts() {
        return products;
    }

    public boolean isLast() {
        return last;
    }
}
