package cart.dao.product;

import cart.entity.product.ProductSaleEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductSaleDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductSaleDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product_sale")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<ProductSaleEntity> rowMapper = (rs, rowNum) ->
            new ProductSaleEntity(
                    rs.getLong("id"),
                    rs.getLong("product_id"),
                    rs.getLong("policy_id")
            );

    public boolean isExistByProductId(final long productId) {
        String sql = "SELECT COUNT(*) FROM product_sale WHERE product_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, productId);
        return count > 0;
    }

    public ProductSaleEntity findByProductId(final long productId) {
        String sql = "SELECT * FROM product_sale WHERE product_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, productId);
    }

    public long save(final long productId, final long policyId) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("product_id", productId);
        parameters.put("policy_id", policyId);

        Number generatedId = simpleJdbcInsert.executeAndReturnKey(parameters);

        return generatedId.longValue();
    }
}
