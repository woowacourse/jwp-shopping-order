package cart.order.dao;

import cart.order.dto.OrderInfoEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderInfoDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    private final RowMapper<OrderInfoEntity> rowMapper = (rs, rowNum) ->
            new OrderInfoEntity(
                    rs.getLong("id"),
                    rs.getLong("order_id"),
                    rs.getLong("product_id"),
                    rs.getString("name"),
                    rs.getLong("price"),
                    rs.getString("image_url"),
                    rs.getLong("quantity")
            );
    
    public OrderInfoDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_info")
                .usingGeneratedKeyColumns("id");
    }
    
    public Long insert(final OrderInfoEntity orderInfoEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("order_id", orderInfoEntity.getOrderId());
        params.put("product_id", orderInfoEntity.getProductId());
        params.put("name", orderInfoEntity.getName());
        params.put("price", orderInfoEntity.getPrice());
        params.put("image_url", orderInfoEntity.getImageUrl());
        params.put("quantity", orderInfoEntity.getQuantity());
        
        return insertAction.executeAndReturnKey(params).longValue();
    }
    
    public List<OrderInfoEntity> findByOrderId(final Long orderId) {
        final String sql = "SELECT * FROM order_info WHERE order_id = ?";
        return jdbcTemplate.query(sql, rowMapper, orderId);
    }
}
