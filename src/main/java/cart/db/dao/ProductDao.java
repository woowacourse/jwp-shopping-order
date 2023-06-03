package cart.db.dao;

import cart.db.entity.ProductEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductDao(JdbcTemplate jdbcTemplate, final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Long create(final ProductEntity productEntity) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(productEntity);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public List<ProductEntity> findAll() {
        String sql = "SELECT * FROM product WHERE is_deleted = false";
        return jdbcTemplate.query(sql, new ProductEntityRowMapper());
    }

    public List<ProductEntity> findByIds(final List<Long> productIds) {
        String sql = "SELECT * FROM product where id in (:ids) AND is_deleted = false";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("ids", productIds);
        return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, new ProductEntityRowMapper());
    }


    public Optional<ProductEntity> findById(final Long productId) {
        String sql = "SELECT * FROM product WHERE id = ? AND is_deleted = false";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new ProductEntityRowMapper(), productId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(final Long productId, final ProductEntity productEntity) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ? AND is_deleted = false";
        jdbcTemplate.update(sql, productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl(), productId);
    }

    public void updateToDelete(final Long productId) {
        String sql = "UPDATE product SET is_deleted = ? WHERE id = ? AND is_deleted = false";
        jdbcTemplate.update(sql, true, productId);
    }

    public void delete(final Long productId) {
        String sql = "DELETE FROM product WHERE id = ? is_deleted = false";
        jdbcTemplate.update(sql, productId);
    }

    private static class ProductEntityRowMapper implements RowMapper<ProductEntity> {
        @Override
        public ProductEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ProductEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url"),
                    rs.getBoolean("is_deleted")
            );
        }
    }
}
