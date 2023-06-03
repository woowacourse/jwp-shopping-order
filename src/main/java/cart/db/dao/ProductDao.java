package cart.db.dao;

import cart.db.entity.ProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            boolean isDeleted = rs.getBoolean("is_deleted");
            return new ProductEntity(productId, name, price, imageUrl, isDeleted);
        });
    }

    public List<ProductEntity> findByIds(final List<Long> productIds) {
        String sql = "SELECT * FROM product where id in (:ids) AND is_deleted = false";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("ids", productIds);
        return namedParameterJdbcTemplate.query(sql, mapSqlParameterSource, (rs, rowNum) -> {
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            boolean isDeleted = rs.getBoolean("is_deleted");
            return new ProductEntity(productId, name, price, imageUrl, isDeleted);
        });
    }


    public ProductEntity findById(final Long productId) {
        String sql = "SELECT * FROM product WHERE id = ? AND is_deleted = false";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            boolean isDeleted = rs.getBoolean("is_deleted");
            return new ProductEntity(productId, name, price, imageUrl, isDeleted);
        });
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
}
