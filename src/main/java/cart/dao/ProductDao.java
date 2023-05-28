package cart.dao;

import cart.domain.Product;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");

    }

    public List<Product> findAllProducts() {
        final String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, prodcutEntityRowMapper());
    }

    public Product findProductById(final Long productId) {
        final String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, prodcutEntityRowMapper(), productId);
    }

    public Long saveProduct(final Product productEntity) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", productEntity.getName())
                .addValue("price", productEntity.getPrice())
                .addValue("image_url", productEntity.getImageUrl());

        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    public void updateProduct(final Long productId, final Product productEntity) {
        final String updateProductQuery
                = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";

        jdbcTemplate.update(updateProductQuery,
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl(),
                productId
        );
    }

    public void deleteProduct(final Long productId) {
        final String deleteProductQuery = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(deleteProductQuery, productId);
    }

    private RowMapper<Product> prodcutEntityRowMapper() {
        return (rs, rowNum) -> {
            final Long productId = rs.getLong("id");
            final String name = rs.getString("name");
            final int price = rs.getInt("price");
            final String imageUrl = rs.getString("image_url");

            return new Product(productId, name, price, imageUrl);
        };
    }
}
