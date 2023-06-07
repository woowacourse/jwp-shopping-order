package cart.repository.dao;

import cart.repository.entity.ProductEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private RowMapper<ProductEntity> productEntityRowMapper = ((rs, rowNum) ->
            new ProductEntity.Builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .price(rs.getInt("price"))
                    .imageUrl(rs.getString("image_url"))
                    .build()
    );

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    public List<ProductEntity> getAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, productEntityRowMapper);
    }

    public Optional<ProductEntity> getProductById(long productId) {
        try {
            String sql = "SELECT * FROM product WHERE id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, productEntityRowMapper, productId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public long createProduct(ProductEntity productEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", productEntity.getId());
        params.put("name", productEntity.getName());
        params.put("price", productEntity.getPrice());
        params.put("image_url", productEntity.getImageUrl());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public void updateProduct(ProductEntity productEntity) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl(), productEntity.getId());
    }

    public void deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
