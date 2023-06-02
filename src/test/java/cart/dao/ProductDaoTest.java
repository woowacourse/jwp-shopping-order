package cart.dao;

import cart.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static cart.fixture.Fixture.PRODUCT;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class ProductDaoTest {
    private final ProductDao productDao;
    @Autowired
    private ProductDaoTest(JdbcTemplate jdbcTemplate){
        this.productDao = new ProductDao(jdbcTemplate);
    }
    @Test
    @DisplayName("모든 상품을 가져온다.")
    void getAllProducts() {
        Assertions.assertThat(productDao.getAllProducts()).hasSize(12);
    }

    @Test
    @DisplayName("상품을 찾아온다.")
    void findById() {
        Assertions.assertThat(productDao.findById(1L).getName()).isEqualTo("제네시스 g80");
    }

    @Test
    @DisplayName("상품을 생성한다")
    void createProduct() {
        Assertions.assertThat(productDao.createProduct(PRODUCT)).isEqualTo(13L);
    }

    @Test
    @DisplayName("상품의 정보를 수정한다.")
    void updateProduct() {
        Product product = new Product("test",2000,"test");
        productDao.updateProduct(1L,product);
        Assertions.assertThat(productDao.findById(1L).getName()).isEqualTo("test");
    }

    @Test
    @DisplayName("상품을 제거한다.")
    void deleteProduct() {
        productDao.deleteProduct(1L);
        Assertions.assertThat(productDao.findById(1L).getDeleted()).isTrue();
    }

    @Test
    @DisplayName("상품의 가격을 가져온다")
    void findPriceById() {
        Assertions.assertThat(productDao.findPriceById(1L)).isEqualTo(1000);
    }
}
