package cart.dao;

import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDao {

    private static final RowMapper<Product> PRODUCT_MAPPER = (rs, rowNum) -> new Product(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    public List<Product> getAllProducts() {
        final String sql = "SELECT id, name, price, image_url FROM product";
        return jdbcTemplate.query(sql, PRODUCT_MAPPER);
    }

    public Product getProductById(final Long productId) {
        final String sql = "SELECT id, name, price, image_url FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, PRODUCT_MAPPER, productId);
    }

    public Long createProduct(final Product product) {
        final SqlParameterSource source = new MapSqlParameterSource()
                .addValue("name", product.getName())
                .addValue("price", product.getPrice())
                .addValue("image_url", product.getImageUrl());

        return jdbcInsert.executeAndReturnKey(source).longValue();
    }

    public void updateProduct(final Long productId, final Product product) {
        final String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), productId);
    }

    public void deleteProduct(final Long productId) {
        final String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
