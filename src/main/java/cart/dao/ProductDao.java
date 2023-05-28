package cart.dao;

import cart.domain.product.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public ProductDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource,
                      final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("product")
                .usingColumns("name", "price", "image_url")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final String name = rs.getString("name");
        final int price = rs.getInt("price");
        final String imageUrl = rs.getString("image_url");

        return new Product(id, name, price, imageUrl);
    };

    public Long save(final Product product) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", product.getNameValue())
                .addValue("price", product.getPriceValue())
                .addValue("image_url", product.getImageUrlValue());

        return jdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<Product> getAllProducts() {
        final String sql = "SELECT id, name, price, image_url FROM product";

        return jdbcTemplate.query(sql, productRowMapper);
    }

    public Product getProductById(final Long productId) {
        final String sql = "SELECT id, name, price, image_url FROM product WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, productRowMapper, productId);
    }

    public List<Product> getProductsByIds(final List<Long> productIds) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", productIds);
        final String sql = "SELECT id, name, price, image_url " +
                "FROM product " +
                "WHERE id IN (:ids)";

        return namedParameterJdbcTemplate.query(sql, parameters, productRowMapper);
    }

    public void updateProduct(final Long productId, final Product product) {
        final String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";

        jdbcTemplate.update(sql, product.getNameValue(), product.getPriceValue(), product.getImageUrlValue(), productId);
    }

    public void deleteProduct(final Long productId) {
        final String sql = "DELETE FROM product WHERE id = ?";

        jdbcTemplate.update(sql, productId);
    }
}
