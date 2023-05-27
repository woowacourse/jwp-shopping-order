package cart.dao;

import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ProductDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }


    public List<Product> getAllProducts() {
        final String sql = "SELECT id, name, price, image_url FROM product";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            final Long productId = rs.getLong("id");
            final String name = rs.getString("name");
            final int price = rs.getInt("price");
            final String imageUrl = rs.getString("image_url");

            return new Product(productId, name, price, imageUrl);
        });
    }

    public Product getProductById(final Long productId) {
        final String sql = "SELECT name, price, image_url FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            return new Product(productId, name, price, imageUrl);
        });
    }

    public Long createProduct(final Product product) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", product.getName())
                .addValue("price", product.getPrice())
                .addValue("image_url", product.getImageUrl());

        return jdbcInsert.executeAndReturnKey(params).longValue();
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
