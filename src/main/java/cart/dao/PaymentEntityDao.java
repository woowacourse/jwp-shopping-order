package cart.dao;

import cart.dao.entity.PaymentEntity;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class PaymentEntityDao {
    public static final RowMapper<PaymentEntity> rowMapper = (rs, rowNum) -> {
        long id = rs.getLong("id");
        long orderId = rs.getLong("order_id");
        int price = rs.getInt("original_total_price");
        return new PaymentEntity(id, orderId, price);
    };

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public PaymentEntityDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("payment")
                .usingGeneratedKeyColumns("id");
    }


    public Long insert(final PaymentEntity paymentEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("order_id", paymentEntity.getOrderId());
        params.put("original_total_price", paymentEntity.getOriginalTotalPrice());
        return this.simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public PaymentEntity findByOrderId(final Long orderId) {
        final String sql = "SELECT * FROM payment WHERE order_id = ?";
        return this.jdbcTemplate.queryForObject(sql, rowMapper, orderId);
    }
}