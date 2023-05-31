package cart.dao;

import cart.domain.Money;
import cart.domain.Product;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {

    private static final RowMapper<Product> ROW_MAPPER = (rs, rowNum) ->
        new Product(rs.getLong("id"),
            rs.getString("name"),
            Money.from(rs.getInt("price")),
            rs.getString("image_url"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("product")
            .usingGeneratedKeyColumns("id");
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Product getProductById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, productId);
    }

    public Long createProduct(Product product) {
        Number generatedKey = insertAction.executeAndReturnKey(Map.of(
            "name", product.getName(),
            "price", product.getPriceIntValue(),
            "image_url", product.getImageUrl()
        ));

        return Objects.requireNonNull(generatedKey).longValue();
    }

    public void updateProduct(Long productId, Product product) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getPriceIntValue(), product.getImageUrl(),
            productId);
    }

    public void deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
