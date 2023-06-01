package cart.dao;

import static cart.fixture.ProductFixture.상품_18900원_엔티티;
import static cart.fixture.ProductFixture.상품_28900원_엔티티;
import static cart.fixture.ProductFixture.상품_8900원_엔티티;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.entity.ProductEntity;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@RepositoryTest
class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    void 상품을_저장한다() {
        // given
        final ProductEntity product = new ProductEntity("허브티", "tea.jpg", 1000L);

        // when
        productDao.insert(product);

        // then
        assertThat(productDao.findAll()).hasSize(1);
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        final ProductEntity savedProduct1 = productDao.insert(상품_8900원_엔티티);
        final ProductEntity savedProduct2 = productDao.insert(상품_18900원_엔티티);

        // when
        final List<ProductEntity> result = productDao.findAll();

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(List.of(savedProduct1, savedProduct2));
    }

    @Test
    void 단일_상품을_조회한다() {
        // given
        final ProductEntity savedProduct = productDao.insert(상품_8900원_엔티티);

        // when
        final ProductEntity result = productDao.findById(savedProduct.getId()).get();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(savedProduct);
    }

    @Test
    void 상품을_수정한다() {
        // given
        final ProductEntity savedProduct = productDao.insert(상품_8900원_엔티티);
        final ProductEntity updatedProduct = new ProductEntity(savedProduct.getId(), "피자", "pizza.png", 10000L);

        // when
        productDao.update(updatedProduct);

        // then
        final ProductEntity result = productDao.findById(updatedProduct.getId()).get();
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("피자"),
                () -> assertThat(result.getImageUrl()).isEqualTo("pizza.png"),
                () -> assertThat(result.getPrice()).isEqualTo(10000L)
        );
    }

    @Test
    void 상품을_삭제한다() {
        // given
        final ProductEntity savedProduct = productDao.insert(상품_8900원_엔티티);

        // when
        productDao.deleteById(savedProduct.getId());

        // then
        assertThat(productDao.findAll()).isEmpty();
    }

    @Test
    void 입력받은_아이디에_대한_상품을_조회한다() {
        // given
        final ProductEntity savedProduct1 = productDao.insert(상품_8900원_엔티티);
        final ProductEntity savedProduct2 = productDao.insert(상품_18900원_엔티티);
        final ProductEntity savedProduct3 = productDao.insert(상품_28900원_엔티티);

        // when
        final List<ProductEntity> result = productDao.findByIds(List.of(savedProduct1.getId(), savedProduct2.getId()));

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(List.of(savedProduct1, savedProduct2));
    }
}
