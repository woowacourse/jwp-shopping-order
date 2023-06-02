package cart.dao;

import cart.entity.ProductEntity;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {

    private final SimpleJdbcInsert jdbcTemplate;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "price", "image_url");
    }

    public List<ProductEntity> findAll() {
        String sql = "SELECT * FROM product";
        try {
            return jdbcTemplate.getJdbcTemplate().query(sql, productMapper());
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public Optional<ProductEntity> findById(Long id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.getJdbcTemplate().queryForObject(sql, productMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long save(ProductEntity productEntity) {
        return jdbcTemplate.executeAndReturnKey(new BeanPropertySqlParameterSource(productEntity)).longValue();
    }

    public void update(ProductEntity productEntity) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.getJdbcTemplate()
                .update(
                        sql,
                        productEntity.getName(),
                        productEntity.getPrice(),
                        productEntity.getImageUrl(),
                        productEntity.getId()
                );
    }

    public void deleteById(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.getJdbcTemplate().update(sql, productId);
    }

    private static RowMapper<ProductEntity> productMapper() {
        return (rs, rowNum) ->
                new ProductEntity(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getString("image_url")
                );
    }
}
