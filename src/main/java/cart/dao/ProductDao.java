package cart.dao;

import cart.domain.Product;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("product")
            .usingGeneratedKeyColumns("id");
    }

    public List<Product> getAllProducts() {
        final String sql = "SELECT * FROM product";
        
        return jdbcTemplate.query(sql, new ProductRowMapper());
    }

    public Optional<Product> getProductById(final Long productId) {
        final String sql = "SELECT * FROM product WHERE id = ?";
        final List<Product> product = jdbcTemplate.query(sql, new ProductRowMapper(), productId);

        if (product.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(product.get(0));
    }

    public Product createProduct(final Product product) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", product.getName());
        params.addValue("price", product.getPrice());
        params.addValue("imageUrl", product.getImageUrl());

        final long productId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Product(productId, product.getName(), product.getPrice(), product.getImageUrl());
    }

    public void updateProduct(final Long productId, final Product product) {
        final String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), productId);
    }

    public void deleteProduct(final Long productId) {
        final String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }

    private static class ProductRowMapper implements RowMapper<Product> {

        @Override
        public Product mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new Product(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("image_url"));
        }
    }
}
