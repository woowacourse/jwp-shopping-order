package cart.product.dao;

import cart.product.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> getAllProducts() {
        final String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            return Product.of(productId, name, price, imageUrl);
        });
    }

    public Product getProductById(final Long productId) {
        final String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            return Product.of(productId, name, price, imageUrl);
        });
    }

    public List<Product> getProductByInterval(final Long lastIdInPrevPage, final int pageItemCount) {
        final String sql = "SELECT * " +
                "FROM product " +
                "WHERE id < ? " +
                "ORDER BY id desc " +
                "limit ?";
        return jdbcTemplate.query(sql, new Object[]{lastIdInPrevPage, pageItemCount}, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            return Product.of(id, name, price, imageUrl);
        });
    }

    public Long getLastProductId() {
        final String sql = "SELECT max(id)" +
                "FROM product";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("max(id)"));
    }

    public Long createProduct(final Product product) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, product.getName());
            ps.setInt(2, product.getPrice());
            ps.setString(3, product.getImageUrl());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
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
