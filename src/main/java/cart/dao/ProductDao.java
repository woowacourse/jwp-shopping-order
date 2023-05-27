package cart.dao;

import cart.dao.entity.ProductEntity;
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

    private static final RowMapper<ProductEntity> ROW_MAPPER = (rs, rowNum) -> new ProductEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
    );

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductEntity> getAllProducts() {
        String sql = "SELECT id, name, price, image_url, created_at, updated_at FROM product";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Optional<ProductEntity> getProductById(Long productId) {
        String sql = "SELECT id, name, price, image_url, created_at, updated_at FROM product WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, ROW_MAPPER, productId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long createProduct(ProductEntity productEntity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO product(name, price, image_url) VALUES(?, ?, ?)";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, productEntity.getName());
                    ps.setInt(2, productEntity.getPrice());
                    ps.setString(3, productEntity.getImageUrl());
                    return ps;
                },
                keyHolder
        );
        return (Long) Objects.requireNonNull(keyHolder.getKeys().get("ID"));
    }

    public void updateProduct(ProductEntity productEntity) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl(), productEntity.getId()
        );
    }

    public void deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
