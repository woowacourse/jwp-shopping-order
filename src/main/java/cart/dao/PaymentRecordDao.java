package cart.dao;

import cart.dao.entity.PaymentRecordEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentRecordDao {

    public static final RowMapper<PaymentRecordEntity> rowMapper = (rs, rowNum) -> {
        long id = rs.getLong("id");
        long orderId = rs.getLong("order_id");
        int price = rs.getInt("original_total_price");
        return new PaymentRecordEntity(id, orderId, price);
    };

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public PaymentRecordDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("payment_record")
            .usingGeneratedKeyColumns("id");
    }


    public long insert(final PaymentRecordEntity paymentRecordEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("order_id", paymentRecordEntity.getOrderId());
        params.put("original_total_price", paymentRecordEntity.getOriginalTotalPrice());
        return this.simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public PaymentRecordEntity findByOrderId(final Long orderId) {
        final String sql = "SELECT * FROM payment_record WHERE order_id = ?";
        return this.jdbcTemplate.queryForObject(sql, rowMapper, orderId);
    }
}