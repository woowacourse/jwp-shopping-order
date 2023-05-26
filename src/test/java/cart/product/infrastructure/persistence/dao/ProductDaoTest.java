package cart.product.infrastructure.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.common.DaoTest;
import cart.product.infrastructure.persistence.entity.ProductEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("ProductDao 은(는)")
@DaoTest
class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    void 상품을_저장한다() {
        // given
        ProductEntity productEntity = new ProductEntity(null, "말랑", 1000, "image");

        // when
        Long id = productDao.save(productEntity);

        // then
        ProductEntity found = productDao.findById(id).get();
        assertThat(found).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(productEntity);
    }

    @Test
    void 상품을_수정한다() {
        // given
        ProductEntity productEntity = new ProductEntity(null, "말랑", 1000, "image");
        Long id = productDao.save(productEntity);
        ProductEntity updated = new ProductEntity(id, "코코닥", 3000, "image");

        // when
        productDao.update(id, updated);

        // then
        ProductEntity found = productDao.findById(id).get();
        assertThat(found).usingRecursiveComparison()
                .isEqualTo(updated);
    }

    @Test
    void 상품을_제거한다() {
        // given
        ProductEntity productEntity = new ProductEntity(null, "말랑", 1000, "image");
        Long id = productDao.save(productEntity);

        // when
        productDao.delete(id);

        // then
        assertThat(productDao.findById(id)).isEmpty();
    }

    @Test
    void 모든_상품을_조회한다() {
        // given
        ProductEntity productEntity = new ProductEntity(null, "말랑", 1000, "image");
        Long id = productDao.save(productEntity);

        // when
        List<ProductEntity> all = productDao.findAll();

        // then
        assertThat(all).hasSize(1);
    }
}
