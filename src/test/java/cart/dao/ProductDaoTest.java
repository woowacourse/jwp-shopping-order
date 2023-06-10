package cart.dao;

import static cart.fixture.ProductFixture.PRODUCT_1;
import static cart.fixture.ProductFixture.PRODUCT_2;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.dao.dto.PageInfo;
import cart.entity.ProductEntity;
import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ProductDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    @DisplayName("전체 상품을 조회한다.")
    void getAllProducts() {
        // given
        Long id1 = productDao.createProduct(PRODUCT_1);
        Long id2 = productDao.createProduct(PRODUCT_2);

        // when
        List<ProductEntity> entities = productDao.getAllProducts();

        // then
        assertThat(entities)
                .extracting(ProductEntity::getId)
                .contains(id1, id2);
    }

    @Test
    @DisplayName("ID로 상품을 조회한다.")
    void getProductById() {
        // given
        Long id = productDao.createProduct(PRODUCT_1);

        // when
        ProductEntity productEntity = productDao.getProductById(id);

        // then
        assertThat(productEntity)
                .usingRecursiveComparison()
                .isEqualTo(new ProductEntity(id,
                        PRODUCT_1.getName(),
                        PRODUCT_1.getPrice(),
                        PRODUCT_1.getImageUrl()));
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void createProduct() {
        // when
        Long id = productDao.createProduct(PRODUCT_1);

        // then
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("상품 정보를 수정한다.")
    void updateProduct() {
        // given
        Long id = productDao.createProduct(PRODUCT_1);

        // when
        productDao.updateProduct(id, PRODUCT_2);

        // then
        ProductEntity entity = productDao.getProductById(id);
        assertThat(entity)
                .usingRecursiveComparison()
                .isEqualTo(new ProductEntity(id,
                        PRODUCT_2.getName(),
                        PRODUCT_2.getPrice(),
                        PRODUCT_2.getImageUrl()));
    }

    @Test
    @DisplayName("상품을 삭제한다.")
    void deleteProduct() {
        // given
        Long id = productDao.createProduct(PRODUCT_1);

        // when, then
        assertDoesNotThrow(
                () -> productDao.deleteProduct(id)
        );
    }

    @Test
    @DisplayName("특정 페이지의 상품들을 조회한다.")
    void findProductsByPage() {
        // given
        Long id = productDao.createProduct(PRODUCT_1);

        // when
        List<ProductEntity> productEntities = productDao.findProductsByPage(1, 1);

        // then
        assertAll(
                () -> assertThat(productEntities)
                        .hasSize(1),
                () -> AssertionsForClassTypes.assertThat(productEntities.get(0))
                        .usingRecursiveComparison()
                        .isEqualTo(new ProductEntity(id,
                                PRODUCT_1.getName(),
                                PRODUCT_1.getPrice(),
                                PRODUCT_1.getImageUrl()))
        );
    }

    @Test
    @DisplayName("특정 페이지의 상품들을 조회할 때의 페이지 정보를 조회한다.")
    void findPageInfo() {
        // given
        productDao.createProduct(PRODUCT_1);

        // when
        PageInfo pageInfo = productDao.findPageInfo(1, 1);

        // then
        assertThat(pageInfo)
                .usingRecursiveComparison()
                .ignoringFields("total", "lastPage")
                .isEqualTo(new PageInfo(1, 1, 1, 1));
    }
}
