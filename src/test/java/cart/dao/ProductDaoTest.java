package cart.dao;

import static cart.fixtures.ProductFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import cart.domain.product.Product;
import cart.exception.ProductNotFoundException;
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
        this.productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    @DisplayName("받은 상품을 삽입한다.")
    void insertProduct() {
        // given
        Product product = CHICKEN.DOMAIN();

        // when, then
        assertThat(productDao.insertProduct(product)).isNotNull();
    }

    @Test
    @DisplayName("상품들을 Limit에 맞게 조회한다.")
    void selectFirstProductsByLimit() {
        // given
        int limit = FIRST_PAGING_PRODUCTS.LIMIT;

        // when
        List<Product> products = productDao.selectFirstProductsByLimit(limit);

        // then
        assertThat(products).usingRecursiveComparison().isEqualTo(FIRST_PAGING_PRODUCTS.PAGING_PRODUCTS);
    }


    @Test
    @DisplayName("상품 ID로 상품 존재 여부 확인 시 상품이 존재하지 않으면 TRUE를 반환한다.")
    void isNotExist_true() {
        // given
        Long notExistId = -1L;

        // when, then
        assertThat(productDao.isNotExistById(notExistId)).isTrue();
    }

    @Test
    @DisplayName("상품 ID로 상품 존재 여부 확인 시 상품이 존재하면 FALSE를 반환한다.")
    void isExist_false() {
        // when, then
        assertThat(productDao.isNotExistById(CHICKEN.ID)).isFalse();
    }

    @Test
    @DisplayName("마지막 상품 ID 다음의 상품들을 LIMIT에 맞게 조회한다.")
    void selectProductsByIdAndLimit() {
        // given
        Long lastId = SALAD.ID;
        int limit = NEXT_PAGING_PRODUCTS.LIMIT;

        // when
        List<Product> products = productDao.selectProductsByIdAndLimit(lastId, limit);

        // then
        assertThat(products).usingRecursiveComparison().isEqualTo(NEXT_PAGING_PRODUCTS.PAGING_PRODUCTS);
    }

    @Test
    @DisplayName("마지막 상품을 조회한다.")
    void selectLastProduct() {
        // when
        Product product = productDao.selectLastProduct();

        // then
        assertThat(product).usingRecursiveComparison().isEqualTo(CHICKEN.ENTITY());
    }

    @Test
    @DisplayName("마지막 상품 조회 시 상품이 존재하지 않으면 예외가 발생한다.")
    void selectLastProduct_throws_not_exist_products() {
        // given
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.update("TRUNCATE TABLE product");

        // when, then
        assertThatThrownBy(() -> productDao.selectLastProduct())
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("최근 상품이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("받은 상품 ID에 해당하는 행을 제거한다.")
    void deleteProduct() {
        // given
        Long deleteProductId = 1L;

        // when
        productDao.deleteProduct(deleteProductId);

        // then
        assertThat(productDao.isNotExistById(deleteProductId)).isTrue();
    }

    @Test
    @DisplayName("받은 상품 ID에 해당하는 행을 수정한다.")
    void updateProduct() {
        // given
        Long productId = CHICKEN.ID;
        String nameToUpdate = CHICKEN.NAME + "UPDATE";
        int priceToUpdate = CHICKEN.PRICE + 10000;
        Product productToUpdate = new Product(nameToUpdate, priceToUpdate, CHICKEN.IMAGE_URL);

        // when
        productDao.updateProduct(productId, productToUpdate);
        Product productAfterUpdate = productDao.getProductById(productId);

        // then
        assertThat(productAfterUpdate).usingRecursiveComparison()
                .ignoringFieldsOfTypes(Long.class).isEqualTo(productToUpdate);
    }
}
