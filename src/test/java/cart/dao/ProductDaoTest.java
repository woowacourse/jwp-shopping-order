package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.entity.ProductEntity;
import cart.domain.Money;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ProductDaoTest {

    private final ProductDao productDao;
    private List<ProductEntity> products;
    private ProductEntity product;

    @Autowired
    public ProductDaoTest(final JdbcTemplate jdbcTemplate) {
        this.productDao = new ProductDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        products = productDao.findAll();
        product = products.get(0);
    }

    @DisplayName("아이디가 일치하는 상품 정보를 조회한다.")
    @Test
    void findById() {
        // given
        final long id = product.getId();

        // when, then
        productDao.findById(id)
                .ifPresentOrElse(
                        found -> assertThat(found.getId()).isEqualTo(id),
                        () -> Assertions.fail("product not exist; productId=" + id));
    }

    @DisplayName("상품 정보를 저장한다.")
    @Test
    void save() {
        // given
        final ProductEntity toSave = new ProductEntity("robin", new Money(10000L), "image.png");

        // when
        final Long savedId = productDao.save(toSave);

        // then
        productDao.findById(savedId)
                .ifPresentOrElse(
                        found -> {
                            assertThat(found.getName()).isEqualTo(toSave.getName());
                            assertThat(found.getPrice()).isEqualTo(toSave.getPrice());
                            assertThat(found.getImageUrl()).isEqualTo(toSave.getImageUrl());
                        },
                        () -> Assertions.fail("product not exist; productId=" + savedId));
    }

    @DisplayName("상품 정보를 변경한다.")
    @Test
    void updateProduct() {
        // given
        final long id = product.getId();
        final ProductEntity toUpdate = new ProductEntity(
                id,
                "doy",
                new Money(20000L),
                "image2.png");

        // when
        productDao.updateProduct(toUpdate);

        // then
        productDao.findById(id)
                .ifPresentOrElse(
                        found -> {
                            assertThat(found.getName()).isEqualTo(toUpdate.getName());
                            assertThat(found.getPrice()).isEqualTo(toUpdate.getPrice());
                            assertThat(found.getImageUrl()).isEqualTo(toUpdate.getImageUrl());
                        },
                        () -> Assertions.fail("product not exist; productId=" + id));
    }

    @Test
    void deleteProduct() {
        // given
        final long id = product.getId();

        // when
        productDao.deleteProduct(id);

        // then
        assertThat(productDao.findById(id)).isEmpty();
    }
}
