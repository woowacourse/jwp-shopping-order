package cart.dao;

import cart.domain.Product;
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

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            Long price = rs.getLong("price");
            String imageUrl = rs.getString("image_url");
            final double pointRatio = rs.getDouble("point_ratio");
            final boolean pointAvailable = rs.getBoolean("point_available");
            return new Product(productId, name, price, imageUrl, pointRatio, pointAvailable);
        });
    }

    public Product getProductById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
            String name = rs.getString("name");
            Long price = rs.getLong("price");
            String imageUrl = rs.getString("image_url");
            final double pointRatio = rs.getDouble("point_ratio");
            final boolean pointAvailable = rs.getBoolean("point_available");
            return new Product(productId, name, price, imageUrl, pointRatio, pointAvailable);
        });
    }

    public Long createProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url, point_ratio, point_available) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, product.getName());
            ps.setLong(2, product.getPrice());
            ps.setString(3, product.getImageUrl());
            ps.setDouble(4, product.getPointRatio());
            ps.setBoolean(5, product.getPointAvailable());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void updateProduct(Long productId, Product product) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ?, point_ratio = ?, point_available = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), product.getPointRatio(), product.getPointAvailable(), productId);
    }

    public void deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
