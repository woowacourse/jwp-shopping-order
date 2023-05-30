package cart.application;

import cart.dao.ProductDao;
import cart.domain.Product;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cart.fixtures.ProductFixtures.CHICKEN;
import static cart.fixtures.ProductFixtures.PANCAKE;
import static cart.fixtures.ProductFixtures.PIZZA;
import static cart.fixtures.ProductFixtures.SALAD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductDao productDao;

    @Test
    void 모든_상품을_가져온다() {
        // given
        when(productDao.getAllProducts()).thenReturn(List.of(CHICKEN.ENTITY));

        // when
        final List<Product> products = productService.getAllProducts();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(products).hasSize(1);
            softAssertions.assertThat(products.get(0).getId()).isEqualTo(CHICKEN.ID);
            softAssertions.assertThat(products.get(0).getName()).isEqualTo(CHICKEN.NAME);
            softAssertions.assertThat(products.get(0).getPrice()).isEqualTo(CHICKEN.PRICE);
            softAssertions.assertThat(products.get(0).getImageUrl()).isEqualTo(CHICKEN.IMAGE_URL);
        });
    }

    @Test
    void 상품_id를_통해_상품을_가져오다() {
        // given
        when(productDao.getProductById(1L)).thenReturn(CHICKEN.ENTITY);

        // when
        final Product product = productService.getProductById(1L);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(product.getId()).isEqualTo(CHICKEN.ID);
            softAssertions.assertThat(product.getName()).isEqualTo(CHICKEN.NAME);
            softAssertions.assertThat(product.getPrice()).isEqualTo(CHICKEN.PRICE);
            softAssertions.assertThat(product.getImageUrl()).isEqualTo(CHICKEN.IMAGE_URL);
        });
    }

    @Nested
    class getProductsInPaging_테스트 {

        @Test
        void 첫페이지_상품_페이지를_가져오다() {
            // given
            when(productDao.getLastProductId()).thenReturn(4L);
            when(productDao.getProductByInterval(5L, 2))
                    .thenReturn(List.of(PANCAKE.ENTITY, PIZZA.ENTITY));

            // when
            final List<Product> products = productService.getProductsInPaging(0L, 2);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(products).hasSize(2);
                softAssertions.assertThat(products.get(0)).isEqualTo(PANCAKE.ENTITY);
                softAssertions.assertThat(products.get(1)).isEqualTo(PIZZA.ENTITY);
            });
        }

        @Test
        void 첫페이지가_아닌_상품_페이지를_가져오다() {
            // given
            when(productDao.getProductByInterval(4L, 2))
                    .thenReturn(List.of(PIZZA.ENTITY, SALAD.ENTITY));

            // when
            final List<Product> products = productService.getProductsInPaging(4L, 2);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(products).hasSize(2);
                softAssertions.assertThat(products.get(0)).isEqualTo(PIZZA.ENTITY);
                softAssertions.assertThat(products.get(1)).isEqualTo(SALAD.ENTITY);
            });
        }
    }

    @Nested
    class hasLastProduct_테스트 {

        @Test
        void 마지막_상품을_가지고_있으면_True를_반환하다() {
            // given
            when(productDao.getProductByInterval(2L, 2))
                    .thenReturn(List.of(CHICKEN.ENTITY));

            // when
            final boolean hasLastProduct = productService.hasLastProduct(2L, 2);

            // then
            assertThat(hasLastProduct).isTrue();
        }

        @Test
        void 마지막_상품을_가지고_있지_않으면_False를_반환하다() {
            // given
            when(productDao.getProductByInterval(4L, 2))
                    .thenReturn(List.of(PIZZA.ENTITY, SALAD.ENTITY));

            // when
            final boolean hasLastProduct = productService.hasLastProduct(4L, 2);

            // then
            assertThat(hasLastProduct).isFalse();
        }
    }

    @Test
    void 상품을_생성하다() {
        // given
        final Product product = new Product("test", 10000, "www.test.com");
        when(productDao.createProduct(product)).thenReturn(5L);

        // when
        final Long productId = productService.createProduct(product);

        // then
        assertThat(productService.createProduct(product)).isEqualTo(5L);
    }

    @Test
    void 상품_정보를_수정하다() {
        // given
        final Product product = new Product("test", 10000, "www.test.com");
        doNothing().when(productDao).updateProduct(1L, product);

        // when, then
        assertDoesNotThrow(() -> productService.updateProduct(1L, product));
    }

    @Test
    void 상품을_삭제하다() {
        // given
        doNothing().when(productDao).deleteProduct(1L);

        // when, then
        assertDoesNotThrow(() -> productService.deleteProduct(1L));
    }
}
