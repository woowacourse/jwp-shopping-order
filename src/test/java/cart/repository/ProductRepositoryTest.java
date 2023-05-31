package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.entity.ProductEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    private ProductRepository productRepository;

    @Mock
    private ProductDao productDao;

    @Test
    void 상품을_저장한다() {
        // given
        Product product = new Product("밀리", 1000000, "http://millie.com");
        given(productDao.save(any())).willReturn(1L);

        // when
        Product savedProduct = productRepository.save(product);

        // then
        assertThat(savedProduct.getId()).isEqualTo(1L);
    }

    @Test
    void 상품을_id로_조회한다() {
        // given
        ProductEntity productEntity = new ProductEntity("밀리", BigDecimal.valueOf(1000000), "http://millie.com");
        given(productDao.findById(1L)).willReturn(Optional.of(productEntity));

        // when
        Product findProduct = productRepository.findById(1L).get();

        // then
        assertThat(findProduct)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(new Product("밀리", 1000000, "http://millie.com"));
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        given(productDao.findAll()).willReturn(List.of(
                new ProductEntity(1L, "밀리", BigDecimal.valueOf(100000000), "http://millie.com"),
                new ProductEntity(2L, "박스터", BigDecimal.valueOf(100), "http://boxster.com"),
                new ProductEntity(3L, "햄스터", BigDecimal.valueOf(10000000), "http://hamster.com")
        ));

        // when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).map(Product::getId)
                .isEqualTo(List.of(1L, 2L, 3L));
    }

    @Test
    void 상품의_정보를_수정한다() {
        // given
        Product updateProduct = new Product(1L, "밀리", BigDecimal.valueOf(10000), "http://millie.com");

        // when
        productRepository.updateProduct(updateProduct);

        // then
        verify(productDao).updateProduct(any());
    }

    @Test
    void 상품을_삭제한다() {
        // given
        Product product = new Product(1L, "밀리", BigDecimal.valueOf(100000000), "http://millie.com");

        // when
        productRepository.deleteProduct(product.getId());

        // then
        verify(productDao).deleteProduct(eq(product.getId()));
    }
}
