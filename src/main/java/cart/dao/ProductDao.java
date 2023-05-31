package cart.dao;

import cart.dao.entity.ProductEntity;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {

    private static final RowMapper<ProductEntity> ROW_MAPPER = (rs, rowNum) ->
        new ProductEntity(rs.getLong("id"),
            rs.getString("name"),
            rs.getString("image_url"),
            rs.getInt("price"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("product")
            .usingGeneratedKeyColumns("id");
    }

    public List<ProductEntity> findAll() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Optional<ProductEntity> findById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, productId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public long save(ProductEntity productEntity) {
        Number generatedKey = insertAction.executeAndReturnKey(Map.of(
            "name", productEntity.getName(),
            "price", productEntity.getPrice(),
            "image_url", productEntity.getImageUrl()
        ));

        return Objects.requireNonNull(generatedKey).longValue();
    }

    public void updateProduct(Long productId, ProductEntity productEntity) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl(), productId);
    }

    public void deleteById(long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }

    public boolean isNonExistingId(long productId) {
        String sql = "SELECT EXISTS(SELECT id FROM product WHERE id = ?) "
            + "AS product_exist";
        try {
            return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sql, new Object[]{productId}, Boolean.class));
        } catch (EmptyResultDataAccessException exception) {
            return true;
        }
    }
}
