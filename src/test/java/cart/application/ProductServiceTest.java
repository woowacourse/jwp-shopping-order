package cart.application;

import static cart.fixture.PageInfoFixture.PAGE_INFO_1;
import static cart.fixture.ProductFixture.PRODUCT_1;
import static cart.fixture.ProductFixture.PRODUCT_1_2;
import static cart.fixture.ProductFixture.PRODUCT_2;
import static cart.fixture.ProductFixture.PRODUCT_3;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;

import cart.dao.MemberDao;
import cart.domain.Product;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.dto.product.ProductsResponse;
import cart.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(ProductService.class)
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    MemberDao memberDao;

    @Test
    @DisplayName("특정 페이지의 상품들을 조회한다.")
    void getProducts() {
        // given
        List<Product> products = List.of(PRODUCT_1, PRODUCT_2, PRODUCT_3);
        willReturn(products).given(productRepository).findProductsByPage(1, 1);
        willReturn(PAGE_INFO_1).given(productRepository).findPageInfo(1, 1);

        // when
        ProductsResponse response = productService.getProducts(1, 1);

        // then
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(ProductsResponse.of(products, PAGE_INFO_1));
    }

    @Test
    @DisplayName("특정 상품을 조회한다.")
    void getProductById() {
        // given
        willReturn(PRODUCT_1).given(productRepository).findProductById(PRODUCT_1.getId());

        // when
        ProductResponse response = productService.getProductById(PRODUCT_1.getId());

        // then
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(ProductResponse.from(PRODUCT_1));
    }

    @Test
    @DisplayName("상품을 등록한다.")
    void createProduct() {
        // given
        ProductRequest request = new ProductRequest(PRODUCT_1.getName(), PRODUCT_1.getPrice(),
                PRODUCT_1.getImageUrl());
        willReturn(PRODUCT_1.getId()).given(productRepository).createProduct(any());

        // when
        Long id = productService.createProduct(request);

        // then
        assertThat(id).isEqualTo(PRODUCT_1.getId());
    }

    @Test
    @DisplayName("상품 정보를 수정한다.")
    void updateProduct() {
        // given
        ProductRequest request = new ProductRequest(PRODUCT_1_2.getName(), PRODUCT_1_2.getPrice(),
                PRODUCT_1_2.getImageUrl());
        willDoNothing().given(productRepository).updateProduct(anyLong(), any());

        // when, then
        assertDoesNotThrow(
                () -> productService.updateProduct(PRODUCT_1.getId(), request)
        );
    }

    @Test
    @DisplayName("상품을 삭제한다.")
    void deleteProduct() {
        // given
        willDoNothing().given(productRepository).deleteProduct(anyLong());

        // when, then
        assertDoesNotThrow(
                () -> productService.deleteProduct(PRODUCT_1.getId())
        );
    }
}
