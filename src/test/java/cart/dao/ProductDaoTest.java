package cart.dao;

import cart.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@ContextConfiguration(classes = ProductDao.class)
class
ProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProductDao productDao;

    @DisplayName("모든 product를 조회한다.")
    @Test
    void getAllProducts() {
        // given
        int beforeCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "product");
        insertProduct(new Product("test1", 0, "test1", 0));
        insertProduct(new Product("test2", 0, "test2", 0));

        // when
        List<Product> products = productDao.getAllProducts();

        // then
        assertEquals(beforeCount + 2, products.size());
        for (Product product : products) {
            assertNotNull(product.getId());
            assertNotNull(product.getName());
            assertNotNull(product.getPrice());
            assertNotNull(product.getImageUrl());
            assertNotNull(product.getStock());
        }
    }

    @DisplayName("id로 product를 조회한다.")
    @Test
    void getProductById() {
        // given
        Long productId = insertProduct(new Product("Test Product", 100, "test_image.jpg", 10));

        // when
        Product product = productDao.getProductById(productId).get();

        // then
        assertEquals(productId, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(100, product.getPrice());
        assertEquals("test_image.jpg", product.getImageUrl());
        assertEquals(10, product.getStock());
    }

    @DisplayName("없는 id로 조회하면 Optional empty를 반환한다.")
    @Test
    void getProductByIdEmpty() {
        // given
        Long productId = 99999999999L;

        // when, then
        assertEquals(Optional.empty(), productDao.getProductById(productId));
    }

    @DisplayName("product를 생성한다.")
    @Test
    void createProduct() {
        // given
        Product product = new Product(null, "New Product", 200, "new_image.jpg", 5);

        // when
        Long generatedId = productDao.createProduct(product);

        // then
        Product productById = productDao.getProductById(generatedId).get();
        assertEquals(product.getName(), productById.getName());
        assertEquals(product.getStock(), productById.getStock());
        assertEquals(product.getPrice(), productById.getPrice());
    }

    @DisplayName("product를 갱신한다.")
    @Test
    void updateProduct() {
        // given
        Long productId = insertProduct(new Product("Test Product", 100, "test_image.jpg", 10));

        // when
        Product updatedProduct = new Product(productId, "Updated Product", 150, "updated_image.jpg", 8);
        productDao.updateProduct(productId, updatedProduct);

        // then
        Product product = productDao.getProductById(productId).get();
        assertEquals(updatedProduct.getName(), product.getName());
        assertEquals(updatedProduct.getPrice(), product.getPrice());
        assertEquals(updatedProduct.getImageUrl(), product.getImageUrl());
        assertEquals(updatedProduct.getStock(), product.getStock());
    }

    @DisplayName("product를 제거한다.")
    @Test
    void deleteProduct() {
        // given
        Long productId = insertProduct(new Product("Test Product", 100, "test_image.jpg", 10));

        // when
        productDao.deleteProduct(productId);

        // then
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "product", "id = " + productId));
    }

    @DisplayName("product를 임의로 추가한다.")
    private Long insertProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url, stock) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, product.getName());
            ps.setInt(2, product.getPrice());
            ps.setString(3, product.getImageUrl());
            ps.setInt(4, product.getStock());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
