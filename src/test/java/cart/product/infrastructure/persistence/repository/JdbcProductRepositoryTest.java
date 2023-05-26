package cart.product.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

import cart.product.domain.Product;
import cart.product.infrastructure.persistence.dao.ProductDao;
import cart.product.infrastructure.persistence.entity.ProductEntity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("JdbcProductRepository 은(는)")
class JdbcProductRepositoryTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private JdbcProductRepository productRepository;

    @Test
    void 상품을_저장한다() {
        // given
        Product product = new Product("말랑", 100, "image");
        given(productDao.save(any()))
                .willReturn(1L);

        // when
        Long id = productRepository.save(product);

        // then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 상품을_수정한다() {
        // given
        Product product = new Product(1L, "말랑", 100, "image");
        doNothing().when(productDao)
                .update(eq(1L), any());

        // when
        productRepository.update(product);

        // then
        then(productDao).should(times(1))
                .update(eq(1L), any());
    }

    @Test
    void 상품을_제거한다() {
        // when
        productRepository.delete(1L);

        // then
        then(productDao).should(times(1))
                .delete(1L);
    }

    @Test
    void 단일_상품을_조회한다() {
        // given
        ProductEntity product = new ProductEntity(1L, "말랑", 100, "image");
        given(productDao.findById(1L))
                .willReturn(Optional.of(product));

        // when
        Product found = productRepository.findById(1L);

        // then
        assertAll(
                () -> assertThat(found.getId()).isEqualTo(1L),
                () -> assertThat(found.getName()).isEqualTo("말랑"),
                () -> assertThat(found.getPrice()).isEqualTo(100),
                () -> assertThat(found.getImageUrl()).isEqualTo("image")
        );
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        ProductEntity product1 = new ProductEntity(1L, "말랑", 100, "image");
        ProductEntity product2 = new ProductEntity(2L, "코코닥", 100, "image");
        List<ProductEntity> products = List.of(product1, product2);
        given(productDao.findAll())
                .willReturn(products);

        // when
        List<Product> all = productRepository.findAll();

        // then
        List<Product> expected = List.of(
                new Product(1L, "말랑", 100, "image"),
                new Product(2L, "코코닥", 100, "image")
        );
        assertThat(all).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
