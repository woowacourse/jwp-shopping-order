package cart.dto.product;

import cart.domain.Product;
import cart.dto.PaginationInfoDto;
import java.util.List;
import org.springframework.data.domain.Page;

public class PagedProductsResponse {

    private final List<ProductResponse> products;
    private final PaginationInfoDto pagination;

    public PagedProductsResponse(final List<ProductResponse> products, final PaginationInfoDto pagination) {
        this.products = products;
        this.pagination = pagination;
    }

    public static PagedProductsResponse from(Page<Product> page) {
        final List<ProductResponse> products = ProductResponse.from(page.getContent());
        final PaginationInfoDto paginationInfo = new PaginationInfoDto(
                (int) page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.getTotalPages()
        );

        return new PagedProductsResponse(products, paginationInfo);
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public PaginationInfoDto getPagination() {
        return pagination;
    }
}
