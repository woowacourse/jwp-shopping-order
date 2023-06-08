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
        final boolean isDeleted = rs.getBoolean("is_deleted");
        return new ProductEntity(productId, name, imageUrl, price, isDeleted);
    };

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductEntity> getNotDeletedProducts() {
        String sql = "SELECT id, name, image_url, price, is_deleted FROM product "
            + "WHERE is_deleted = 0";
        return jdbcTemplate.query(sql, productEntityRowMapper);
    }

    public List<ProductEntity> getProductsByPage(final int page, final int size) {
        final int offset = calculateOffset(page, size);
        final String sql = "SELECT id, name, image_url, price, is_deleted FROM product "
            + "ORDER BY id LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, productEntityRowMapper, size, offset);
    }

    public long getAllProductCount() {
        final String sql = "SELECT COUNT(*) FROM product";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public Optional<ProductEntity> getProductById(final Long productId) {
        String sql = "SELECT id, name, image_url, price, is_deleted FROM product WHERE id = ? and is_deleted = 0";
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

    public boolean existById(final Long id) {
        final String sql = "SELECT COUNT(*) FROM product WHERE id = ? and is_deleted = 0";
        final long count = jdbcTemplate.queryForObject(sql, Long.class, id);
        return count > 0;
    }

    public int updateProductDeleted(final Long productId) {
        final String sql = "UPDATE product SET is_deleted = 1 WHERE id = ?";
        return jdbcTemplate.update(sql, productId);
    }

    private int calculateOffset(final int page, final int size) {
        if (page == 1) {
            return 0;
        }
        return (page - 1) * size;
    }
}
