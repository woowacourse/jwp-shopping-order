package cart.dao;

import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public List<Product> findAllProducts() {
        final String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, prodcutRowMapper());
    }

    public List<Product> findByIds(final List<Long> ids) {
        final String findByIdsQuery = "SELECT * FROM product WHERE id IN (:productIds)";
        final MapSqlParameterSource parameters = new MapSqlParameterSource("productIds", ids);

        return namedParameterJdbcTemplate.query(findByIdsQuery, parameters, prodcutRowMapper());
    }

    public Optional<Product> findProductById(final Long productId) {
        final String sql = "SELECT * FROM product WHERE id = ?";
        final List<Product> products = jdbcTemplate.query(sql, prodcutRowMapper(), productId);

        if (products.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(products.get(0));
    }

    public Long saveProduct(final Product product) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", product.getName())
                .addValue("price", product.getPrice())
                .addValue("image_url", product.getImageUrl());

        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    public void updateProduct(final Long productId, final Product product) {
        final String updateProductQuery
                = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";

        jdbcTemplate.update(updateProductQuery,
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                productId
        );
    }

    public void deleteProduct(final Long productId) {
        final String deleteProductQuery = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(deleteProductQuery, productId);
    }

    private RowMapper<Product> prodcutRowMapper() {
        return (rs, rowNum) -> {
            final Long productId = rs.getLong("id");
            final String name = rs.getString("name");
            final int price = rs.getInt("price");
            final String imageUrl = rs.getString("image_url");

            return new Product(productId, name, price, imageUrl);
        };
    }
}
