package cart.dao.product;

import cart.domain.product.Product;
import cart.entity.product.ProductEntity;
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

    public List<ProductEntity> getAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Boolean isOnSale = rs.getBoolean("isOnSale");
            int salePrice = rs.getInt("salePrice");
            return new ProductEntity(productId, name, price, imageUrl, isOnSale, salePrice);
        });
    }

    public ProductEntity getProductById(final Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Boolean isOnSale = rs.getBoolean("isOnSale");
            int salePrice = rs.getInt("salePrice");
            return new ProductEntity(productId, name, price, imageUrl, isOnSale, salePrice);
        });
    }

    public Long createProduct(final Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url, isOnSale, salePrice) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, product.getName());
            ps.setInt(2, product.getPrice());
            ps.setString(3, product.getImageUrl());
            ps.setBoolean(4, false);
            ps.setInt(5, 0);

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void updateProduct(final Long productId, final Product product) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), productId);
    }

    public void deleteProduct(final Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }

    public void applySalePolicy(final long productId, final boolean isOnSale) {
        String sql = "UPDATE product SET isOnSale = ? WHERE id = ?";
        jdbcTemplate.update(sql, isOnSale, productId);
    }

    public void updateSaleAmount(final long productId, final int salePrice) {
        String sql = "UPDATE product SET salePrice = ? WHERE id = ?";
        jdbcTemplate.update(sql, salePrice, productId);
    }
}
