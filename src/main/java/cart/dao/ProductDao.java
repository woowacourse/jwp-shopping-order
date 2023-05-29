package cart.dao;

import cart.dao.entity.ProductEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "price", "image_url");
    }

    public List<ProductEntity> getAllProducts() {
        String sql = "SELECT id, name, price, image_url, created_at, updated_at FROM product";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Optional<ProductEntity> getProductById(Long productId) {
        String sql = "SELECT id, name, price, image_url, created_at, updated_at FROM product WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, productId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long createProduct(ProductEntity productEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(productEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
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
