package cart.dto.product;

import cart.dao.dto.PageInfo;
import cart.domain.Product;
import cart.dto.page.PageResponse;
import java.util.List;
import java.util.stream.Collectors;

public class ProductsResponse {
    private final List<ProductResponse> products;
    private final PageResponse pagination;

    private ProductsResponse(final List<ProductResponse> products, final PageResponse pagination) {
        this.products = products;
        this.pagination = pagination;
    }

    public static ProductsResponse of(final List<Product> products, final PageInfo pageInfo) {
        List<ProductResponse> productResponses = products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        PageResponse pageResponse = PageResponse.from(pageInfo);
        return new ProductsResponse(productResponses, pageResponse);
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public PageResponse getPagination() {
        return pagination;
    }
}
