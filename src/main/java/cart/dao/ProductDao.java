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
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            boolean isDiscounted = (rs.getInt("is_discounted") == 1) ? true : false;
            int discountRate = rs.getInt("discount_rate");

            return new Product(productId, name, price, imageUrl, isDiscounted, discountRate);
        });
    }

    public Product getProductById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            boolean isDiscounted = (rs.getInt("is_discounted") == 1) ? true : false;
            int discountRate = rs.getInt("discount_rate");

            return new Product(productId, name, price, imageUrl, isDiscounted, discountRate);
        });
    }

    public Product getProductByName(String name) {
        String sql = "SELECT * FROM product WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            boolean isDiscounted = (rs.getInt("is_discounted") == 1) ? true : false;
            int discountRate = rs.getInt("discount_rate");

            return new Product(id, name, price, imageUrl, isDiscounted, discountRate);
        }, name);
    }

    public Long createProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url, is_discounted, discount_rate) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, product.getName());
            ps.setInt(2, product.getPrice());
            ps.setString(3, product.getImageUrl());
            ps.setInt(4, product.getIsDiscounted() ? 1 : 0);
            ps.setInt(5, product.getDiscountRate());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void updateProduct(Long productId, Product product) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ?, is_discounted = ?, discount_rate = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), product.getIsDiscounted(), product.getDiscountRate(), productId);
    }

    public void deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
