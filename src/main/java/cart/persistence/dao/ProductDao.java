package cart.persistence.dao;

import cart.persistence.entity.ProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private static final RowMapper<ProductEntity> rowMapper =
            (rs, rowNum) -> new ProductEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url")
            );

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(ProductEntity productEntity) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(productEntity);

        return simpleJdbcInsert.executeAndReturnKey(param).longValue();
    }

    public List<ProductEntity> findAll() {
        String sql = "SELECT * FROM product";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<ProductEntity> findById(Long id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        ProductEntity findProduct = jdbcTemplate.queryForObject(sql, rowMapper, id);

        return Optional.ofNullable(findProduct);
    }

    public int updateProduct(Long productId, ProductEntity productEntity) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";

        return jdbcTemplate.update(sql, productEntity.getName(), productEntity.getPrice(),
                productEntity.getImageUrl(), productId);
    }

    public void deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
