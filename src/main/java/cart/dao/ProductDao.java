package cart.dao;

import cart.entity.ProductEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<ProductEntity> rowMapper = (rs, rowNum) ->
            new ProductEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("image_url"),
                    rs.getInt("price")
            );

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    public List<ProductEntity> getAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<ProductEntity> getProductById(final Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, productId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Long createProduct(final ProductEntity product) {
        final Map<String, Object> parameters = new HashMap<>();

        parameters.put("name", product.getName());
        parameters.put("price", product.getPrice());
        parameters.put("image_url", product.getImage());

        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    public void updateProduct(Long productId, ProductEntity product) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                product.getName(),
                product.getPrice(),
                product.getImage(),
                productId
        );
    }

    public void deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
