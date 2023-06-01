package cart.dto;

import java.util.List;

public class PagedProductsResponse {

    private final List<ProductResponse> products;
    private final PaginationInfoDto pagination;

    public PagedProductsResponse(final List<ProductResponse> products, final PaginationInfoDto pagination) {
        this.products = products;
        this.pagination = pagination;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public PaginationInfoDto getPagination() {
        return pagination;
    }
}
