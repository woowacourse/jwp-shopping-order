package cart.dao;

import cart.dao.entity.PaymentRecordEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class PaymentRecordDao {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public PaymentRecordDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("payment_record")
                .usingGeneratedKeyColumns("id");
    }


    public long insert(final PaymentRecordEntity paymentRecordEntity) {
        final BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(paymentRecordEntity);
        return this.simpleJdbcInsert.executeAndReturnKey(beanPropertySqlParameterSource).longValue();
    }

    public PaymentRecordEntity findByOrderId(final Long orderId) {
        final String sql = "SELECT * FROM payment_record WHERE order_id = ?";
        return this.jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            return new PaymentRecordEntity(rs.getLong("id"), rs.getLong("order_id"), rs.getInt("total_price"));
        }, orderId);
    }
}