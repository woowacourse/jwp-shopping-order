package cart.application;

import static cart.fixture.ProductFixture.CHICKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.exception.ProductNotFound;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("ProductService 은(는)")
class ProductServiceTest {

    private static final ProductRequest PRODUCT_REQUEST = new ProductRequest("치킨", 10_000, "www.naver.com");

    private ProductDao productDao = mock(ProductDao.class);
    private ProductService productService = new ProductService(productDao);

    @Test
    void 단일_상품을_반환한다() {
        // given
        Product expected = new Product(1L, "치킨", 10_000, "www.naver.com");
        given(productDao.findById(1L))
                .willReturn(Optional.of(expected));

        // when
        ProductResponse actual = productService.findById(1L);

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 상품_조회시_없으면_예외() {
        // given
        given(productDao.findById(1L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.findById(1L))
                .isInstanceOf(ProductNotFound.class);
    }

    @Test
    void 모든_상품을_반환한다() {
        // given
        List<Product> expected = List.of(
                new Product(1L, "치킨", 10_000, "www.naver.com"),
                new Product(2L, "피자", 20_000, "www.kakao.com")

        );
        given(productDao.findAll())
                .willReturn(expected);

        // when
        List<ProductResponse> actual = productService.findAll();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 상품을_생성한다() {
        // given
        given(productDao.save(any()))
                .willReturn(1L);

        // when
        Long actual = productService.createProduct(PRODUCT_REQUEST);

        // then
        assertThat(actual).isEqualTo(1L);
    }

    @Test
    void 상품을_갱신한다() {
        // given
        given(productDao.findById(1L))
                .willReturn(Optional.of(CHICKEN));

        // when & then
        assertThatNoException().isThrownBy(() -> productService.updateProduct(1L, PRODUCT_REQUEST));
    }

    @Test
    void 상품을_갱신시_상품이_없으면_예외() {
        // given
        given(productDao.findById(1L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.updateProduct(1L, PRODUCT_REQUEST))
                .isInstanceOf(ProductNotFound.class);
    }

    @Test
    void 상품을_삭제한다() {
        given(productDao.findById(1L))
                .willReturn(Optional.of(CHICKEN));

        // when && then
        assertThatNoException().isThrownBy(() -> productService.deleteProduct(1L));
    }

    @Test
    void 상품을_삭제시_상품이_없으면_예외() {
        // given
        given(productDao.findById(1L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.deleteProduct(1L))
                .isInstanceOf(ProductNotFound.class);
    }
}
