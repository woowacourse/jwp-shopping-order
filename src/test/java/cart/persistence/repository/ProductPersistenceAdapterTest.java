package cart.persistence.repository;

import cart.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Sql({"classpath:truncate.sql", "classpath:data.sql"})
class ProductPersistenceAdapterTest {

    private ProductPersistenceAdapter productPersistenceAdapter;

    private Product product;

    @Autowired
    public ProductPersistenceAdapterTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.productPersistenceAdapter = new ProductPersistenceAdapter(namedParameterJdbcTemplate);
        this.product = new Product(1L, "고기", 10000, "https://", 10.0, true);
    }

    @Test
    @DisplayName("상품을 삽입할 수 있다")
    void insert() {
        // given, when
        Product inserted = productPersistenceAdapter.insert(product);
        // when
        assertThat(inserted).isNotNull();
    }

    @Test
    @DisplayName("상품을 ID를 통해 찾을 수 있다")
    void findById() {
        // given
        Product inserted = productPersistenceAdapter.insert(product);
        // when
        Optional<Product> found = productPersistenceAdapter.findById(inserted.getId());
        // then
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("모든 상품을 찾을 수 있다")
    void findAll() {
        // given
        productPersistenceAdapter.insert(product);
        // when
        List<Product> found = productPersistenceAdapter.findAll();
        // then
        assertThat(found).isNotEmpty();
    }

    @Test
    @DisplayName("상품을 업데이트 할 수 있다")
    void update() {
        // given
        Product inserted = productPersistenceAdapter.insert(product);
        // when
        productPersistenceAdapter.update(new Product(inserted.getId(), "회", product.getPrice(),
                product.getImageUrl(), product.getPointRatio(), product.isPointAvailable()));
        Optional<Product> found = productPersistenceAdapter.findById(inserted.getId());
        // then
        assertThat(found.get().getName()).isEqualTo("회");
    }

    @Test
    @DisplayName("상품을 삭제할 수 있다")
    void delete() {
        // given
        Product inserted = productPersistenceAdapter.insert(product);
        // when
        productPersistenceAdapter.delete(inserted.getId());
        Optional<Product> found = productPersistenceAdapter.findById(inserted.getId());
        // then
        assertThat(found).isEmpty();
    }
}
