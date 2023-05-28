package cart.persistence.dao;

import cart.persistence.entity.ProductEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {

    private final RowMapper<ProductEntity> productEntityRowMapper = (rs, rowNum) -> {
        final Long productId = rs.getLong("id");
        final String name = rs.getString("name");
        final String imageUrl = rs.getString("image_url");
        final int price = rs.getInt("price");
        return new ProductEntity(productId, name, imageUrl, price);
    };

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductEntity> getAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, productEntityRowMapper);
    }

    public Optional<ProductEntity> getProductById(final Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, productEntityRowMapper, productId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Long insert(final ProductEntity product) {
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

    public int updateProduct(final Long productId, final ProductEntity product) {
        final String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        return jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), productId);
    }

    public int deleteProduct(final Long productId) {
        final String sql = "DELETE FROM product WHERE id = ?";
        return jdbcTemplate.update(sql, productId);
    }
}
