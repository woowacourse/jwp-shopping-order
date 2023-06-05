package cart.dao;

import static cart.fixture.DomainFixture.CHICKEN;
import static cart.fixture.DomainFixture.SALAD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ProductDaoTest {

    ProductDao productDao;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    @DisplayName("save는 저장할 상품을 전달하면 상품을 저장하고 ID를 반환한다.")
    void saveSuccessTest() {
        Long persistProductId = productDao.save(CHICKEN);

        assertThat(persistProductId).isPositive();
    }

    @Test
    @DisplayName("findAll은 호출하면 존재하는 모든 상품을 반환한다.")
    void findAllTest() {
        List<Product> actual = productDao.findAll();

        assertThat(actual).hasSize(3);
    }

    @Test
    @DisplayName("findById는 존재하는 상품 ID를 전달하면 해당 상품을 반환한다.")
    void findByIdSuccessTest() {
        Optional<Product> actual = productDao.findById(CHICKEN.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getId()).isEqualTo(CHICKEN.getId())
        );
    }

    @Test
    @DisplayName("update는 존재하는 상품 ID와 수정할 정보를 가지고 있는 상품을 전달하면 상품 ID에 해당하는 상품의 정보를 변경한다.")
    void updateSuccessTest() {
        Product targetProduct = CHICKEN;
        Product updateProduct = SALAD;

        productDao.update(targetProduct.getId(), updateProduct);

        Product actual = productDao.findById(targetProduct.getId()).get();

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(updateProduct.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(updateProduct.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(updateProduct.getImageUrl())
        );
    }

    @Test
    @DisplayName("deleteById는 지정한 상품 ID의 상품을 삭제한다.")
    void deleteByIdSuccessTest() {
        productDao.deleteById(CHICKEN.getId());

        Optional<Product> actual = productDao.findById(CHICKEN.getId());
        assertThat(actual).isEmpty();
    }
}
