package cart.dao;

import cart.config.DaoTestConfig;
import cart.dao.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static cart.fixture.entity.ProductEntityFixture.상품_엔티티;
import static org.assertj.core.api.Assertions.assertThat;

class ProductDaoTest extends DaoTestConfig {

    ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void 상품을_저장한다() {
        // when
        Long 계란_식별자값 = productDao.insertProduct(상품_엔티티("계란", 1000));

        // then
        assertThat(계란_식별자값)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 상품_식별자값을_통해_상품을_조회한다() {
        // given
        Long 계란_식별자값 = productDao.insertProduct(상품_엔티티("계란", 1000));

        // when
        Optional<ProductEntity> 아마_계란 = productDao.getByProductId(계란_식별자값);

        // then
        assertThat(아마_계란).contains(
                new ProductEntity(계란_식별자값, "계란", 1000, "https://image.png")
        );
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        Long 계란_식별자값 = productDao.insertProduct(상품_엔티티("계란", 1000));
        Long 치킨_식별자값 = productDao.insertProduct(상품_엔티티("치킨", 20000));

        // when
        List<ProductEntity> 전체_상품_목록 = productDao.getAllProducts();

        // then
        assertThat(전체_상품_목록).contains(
                new ProductEntity(계란_식별자값, "계란", 1000, "https://image.png"),
                new ProductEntity(치킨_식별자값, "치킨", 20000, "https://image.png")
        );
    }

    @Test
    void 상품을_수정한다() {
        // given
        Long 계란_식별자값 = productDao.insertProduct(상품_엔티티("계란", 1000));

        // when
        productDao.updateProduct(계란_식별자값, 상품_엔티티("계란", 20000));

        Optional<ProductEntity> 아마_계란 = productDao.getByProductId(계란_식별자값);

        // then
        assertThat(아마_계란).contains(
                new ProductEntity(계란_식별자값, "계란", 20000, "https://image.png")
        );
    }

    @Test
    void 상품_식별자값을_통해_상품을_삭제한다() {
        // given
        Long 계란_식별자값 = productDao.insertProduct(상품_엔티티("계란", 1000));

        // when
        productDao.deleteByProductId(계란_식별자값);

        Optional<ProductEntity> 아마_계란 = productDao.getByProductId(계란_식별자값);

        // then
        assertThat(아마_계란).isEmpty();
    }
}
