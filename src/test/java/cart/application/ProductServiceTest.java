package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.willReturn;

import cart.domain.Product;
import cart.dto.product.PagedProductsResponse;
import cart.dto.product.ProductResponse;
import cart.repository.dao.ProductDao;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductServiceTest {

    private final ProductDao productDao = mock(ProductDao.class);

    private ProductService productService;
    private final List<Product> products = List.of(
            new Product(1L, "상품1", 1_000, "www.example1.com"),
            new Product(2L, "상품2", 2_000, "www.example2.com"),
            new Product(3L, "상품3", 3_000, "www.example3.com"),
            new Product(4L, "상품4", 4_000, "www.example4.com"),
            new Product(5L, "상품5", 5_000, "www.example5.com"),
            new Product(6L, "상품6", 6_000, "www.example6.com"),
            new Product(7L, "상품7", 7_000, "www.example7.com"),
            new Product(8L, "상품8", 8_000, "www.example8.com"),
            new Product(9L, "상품9", 9_000, "www.example9.com"),
            new Product(10L, "상품10", 10_000, "www.example10.com")
    );

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("페이지 정보에 맞게 정렬된 Product 목록을 반환한다")
    @Test
    void getPagedProducts() {
        // given
        willReturn(products).given(productDao).getAllProducts();
        int unitSize = 3;
        int page = 2;

        // when
        final PagedProductsResponse pagedProducts = productService.getPagedProducts(unitSize, page);

        // then
        assertThat(pagedProducts.getProducts()).map(ProductResponse::getId)
                .containsExactly(7L, 6L, 5L);
        assertThat(pagedProducts.getPagination().getCurrentPage()).isEqualTo(page);
        assertThat(pagedProducts.getPagination().getPerPage()).isEqualTo(unitSize);
        assertThat(pagedProducts.getPagination().getTotal()).isEqualTo(products.size());
        assertThat(pagedProducts.getPagination().getLastPage()).isEqualTo(4);
    }

    @DisplayName("마지막 페이지는 unitSize보다 작은 개수의 Product를 포함할 수 있다")
    @Test
    void getPagedProducts_lastPage_unitSizeOrLess() {
        // given
        willReturn(products).given(productDao).getAllProducts();
        int unitSize = 3;
        int page = 4;

        // when
        final PagedProductsResponse pagedProducts = productService.getPagedProducts(unitSize, page);

        // then
        assertThat(pagedProducts.getProducts()).map(ProductResponse::getId)
                .containsOnly(1L);
    }
}
