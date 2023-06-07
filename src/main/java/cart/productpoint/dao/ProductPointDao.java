package cart.productpoint.dao;

import cart.productpoint.repository.ProductPointEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProductPointDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    private final RowMapper<ProductPointEntity> rowMapper = (rs, rowNum) ->
            new ProductPointEntity(
                    rs.getLong("id"),
                    rs.getDouble("point_ratio"),
                    rs.getBoolean("point_available")
            );
    
    public ProductPointDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product_point")
                .usingGeneratedKeyColumns("id");
    }
    
    public Long createProductPoint(final ProductPointEntity productPointEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("point_ratio", productPointEntity.getPointRatio());
        params.put("point_available", productPointEntity.isPointAvailable());
        
        return insertAction.executeAndReturnKey(params).longValue();
    }
    
    public ProductPointEntity getProductPointById(final Long id) {
        final String sql = "SELECT * FROM product_point WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
    
    public void update(final ProductPointEntity productPointEntity) {
        String sql = "UPDATE product_point SET point_ratio = ?, point_available = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                productPointEntity.getPointRatio(),
                productPointEntity.isPointAvailable(),
                productPointEntity.getId()
        );
    }
    
    public void deleteById(final Long id) {
        String sql = "DELETE FROM product_point WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
