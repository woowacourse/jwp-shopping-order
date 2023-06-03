package cart.application.dto.product;

import java.util.List;

public class ProductPageResponse {

    private final long totalPage;
    private final List<ProductResponse> productResponse;

    public ProductPageResponse(final long totalPage, final List<ProductResponse> productResponse) {
        this.totalPage = totalPage;
        this.productResponse = productResponse;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public List<ProductResponse> getProductResponse() {
        return productResponse;
    }
}
