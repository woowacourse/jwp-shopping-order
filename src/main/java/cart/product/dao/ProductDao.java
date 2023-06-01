package cart.product.dao;

import cart.product.repository.ProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductDao {
    
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    private final RowMapper<ProductEntity> rowMapper = (rs, rowNum) ->
            new ProductEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("price"),
                    rs.getString("image_url"),
                    rs.getDouble("point_ratio"),
                    rs.getBoolean("point_available")
            );
    
    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }
    
    public List<ProductEntity> getAllProducts() {
        final String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, rowMapper);
    }
    
    public ProductEntity getProductById(final Long id) {
        final String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
    
    public Long createProduct(final ProductEntity productEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", productEntity.getName());
        params.put("price", productEntity.getPrice());
        params.put("image_url", productEntity.getImageUrl());
        params.put("point_ratio", productEntity.getPointRatio());
        params.put("point_available", productEntity.isPointAvailable());
        
        return insertAction.executeAndReturnKey(params).longValue();
    }
    
    public void updateProduct(final ProductEntity productEntity) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ?, point_ratio = ?, point_available = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl(),
                productEntity.getPointRatio(),
                productEntity.isPointAvailable(),
                productEntity.getId()
        );
    }
    
    public void deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
