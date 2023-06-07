package cart.dao.product;

import cart.entity.ProductEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static cart.fixture.ProductFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@JdbcTest
@Import({ProductDao.class})
class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    @DisplayName("상품을 등록한다.")
    void save_product() {
        // given
        ProductEntity chickenEntity = chicken;

        // when
        Long id = productDao.createProduct(chickenEntity);
        ProductEntity result = productDao.getProductById(id).get();

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo(chickenEntity.getName());
        assertThat(result.getPrice()).isEqualTo(chickenEntity.getPrice());
        assertThat(result.getImageUrl()).isEqualTo(chickenEntity.getImageUrl());
        assertThat(result.getDiscountRate()).isEqualTo(chickenEntity.getDiscountRate());
    }

    @Test
    @DisplayName("상품 전체를 조회한다.")
    void find_all() {
        // given
        Long chickenId = productDao.createProduct(chicken);
        Long forkId = productDao.createProduct(fork);

        List<ProductEntity> expect = List.of(generateChicken(chickenId), generateFork(forkId));
        // when
        List<ProductEntity> result = productDao.getAllProducts();

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(expect);
    }

    @Test
    @DisplayName("상품을 id로 조회한다.")
    void find_by_id() {
        // given
        Long chickenId = productDao.createProduct(chicken);

        // when
        ProductEntity result = productDao.getProductById(chickenId).get();

        // then
        assertThat(result.getId()).isEqualTo(chickenId);
        assertThat(result.getName()).isEqualTo(chicken.getName());
        assertThat(result.getPrice()).isEqualTo(chicken.getPrice());
        assertThat(result.getImageUrl()).isEqualTo(chicken.getImageUrl());
        assertThat(result.getDiscountRate()).isEqualTo(chicken.getDiscountRate());
    }

    @Test
    @DisplayName("상품정보를 업데이트한다.")
    void update() {
        // given
        Long chickenId = productDao.createProduct(chicken);
        ProductEntity updateEntity = new ProductEntity(chickenId, chicken.getName(), chicken.getPrice(), chicken.getImageUrl(), false, 0);

        // when
        productDao.updateProduct(chickenId, updateEntity);
        ProductEntity result = productDao.getProductById(chickenId).get();

        // then
        assertThat(result.getId()).isEqualTo(chickenId);
        assertThat(result.getName()).isEqualTo(updateEntity.getName());
        assertThat(result.getPrice()).isEqualTo(updateEntity.getPrice());
        assertThat(result.getImageUrl()).isEqualTo(updateEntity.getImageUrl());
        assertThat(result.getDiscountRate()).isEqualTo(updateEntity.getDiscountRate());
    }

    @Test
    @DisplayName("상품을 삭제한다.")
    void delete() {
        // given
        Long chickenId = productDao.createProduct(chicken);

        // when
        productDao.deleteProduct(chickenId);
        Optional<ProductEntity> result = productDao.getProductById(chickenId);

        // then
        assertThat(result.isPresent()).isFalse();
    }
}
