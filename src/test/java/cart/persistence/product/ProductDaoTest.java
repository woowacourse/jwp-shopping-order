package cart.persistence.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static cart.fixture.ProductEntityFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
    @DisplayName("상품을 올바르게 생성한다.")
    void createProductTest() {
        //given
        final ProductEntity productEntity = 배변패드;
        //when,then
        assertDoesNotThrow(() -> productDao.createProduct(productEntity));
    }

    @Test
    @DisplayName("전체 상품 목록을 조회한다")
    void findAllProductsTest() {
        // given
        final Long savedPadId = productDao.createProduct(배변패드);
        final Long savedTailId = productDao.createProduct(비버꼬리요리);
        final Long savedDinoId = productDao.createProduct(디노);

        final ProductEntity savedPad = new ProductEntity(savedPadId, 배변패드.getName(), 배변패드.getPrice(), 배변패드.getImageUrl());
        final ProductEntity savedTail = new ProductEntity(savedTailId, 비버꼬리요리.getName(), 비버꼬리요리.getPrice(), 비버꼬리요리.getImageUrl());
        final ProductEntity savedDino = new ProductEntity(savedDinoId, 디노.getName(), 디노.getPrice(), 디노.getImageUrl());

        // when
        final List<ProductEntity> productEntities = productDao.findAll();

        // then
        assertThat(productEntities).usingRecursiveComparison().isEqualTo(List.of(savedPad, savedTail, savedDino));
    }

    @Test
    @DisplayName("상품 id로 특정 상품 정보를 조회한다.")
    void findProductByIdTest() {
        // given
        final Long savedPadId = productDao.createProduct(배변패드);
        final Long savedTailId = productDao.createProduct(비버꼬리요리);

        final ProductEntity savedPad = new ProductEntity(savedPadId, 배변패드.getName(), 배변패드.getPrice(), 배변패드.getImageUrl());
        final ProductEntity savedTail = new ProductEntity(savedTailId, 비버꼬리요리.getName(), 비버꼬리요리.getPrice(), 비버꼬리요리.getImageUrl());

        // when
        final ProductEntity findTail = productDao.findById(savedTailId);

        // then
        assertThat(findTail).usingRecursiveComparison().isEqualTo(savedTail);
    }
}
