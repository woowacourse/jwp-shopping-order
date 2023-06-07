package cart.dao.product;

import cart.entity.ProductEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ProductEntity> rowMapper = (rs, rowNum) -> new ProductEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url"),
            rs.getBoolean("is_discounted"),
            rs.getInt("discount_rate")
    );

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductEntity> getAllProducts() {
        String sql = "SELECT id, name, price, image_url, is_discounted, discount_rate FROM product";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<ProductEntity> getProductById(final Long productId) {
        String sql = "SELECT id, name, price, image_url, is_discounted, discount_rate FROM product WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, productId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    public Long createProduct(ProductEntity productEntity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url, is_discounted, discount_rate) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, productEntity.getName());
            ps.setInt(2, productEntity.getPrice());
            ps.setString(3, productEntity.getImageUrl());
            ps.setBoolean(4, productEntity.isDiscounted());
            ps.setInt(5, productEntity.getDiscountRate());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void updateProduct(final Long productId, final ProductEntity product) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ?, is_discounted = ?, discount_rate = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.isDiscounted(),
                product.getDiscountRate(),
                productId);
    }

    public void deleteProduct(final Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
