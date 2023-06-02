package cart.dao;

import cart.domain.Payment;
import cart.entity.PaymentEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public PaymentDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("payment")
                .usingGeneratedKeyColumns("id")
                .usingColumns("original_price",
                        "discount_price",
                        "final_price",
                        "order_id",
                        "member_id");
    }

    public Long save(final Payment payment, final Long orderId, final Long memberId) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("original_price", payment.getOriginalPrice().getValue());
        sqlParameterSource.addValue("discount_price", payment.getDiscountPrice().getValue());
        sqlParameterSource.addValue("final_price", payment.getFinalPrice().getValue());
        sqlParameterSource.addValue("order_id", orderId);
        sqlParameterSource.addValue("member_id", memberId);

        return jdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public PaymentEntity findByOrderId(final Long orderId) {
        String sql = "SELECT * FROM payment WHERE order_id = ?";
        return jdbcTemplate.queryForObject(sql, paymentEntityRowMapper(), orderId);
    }

    private RowMapper<PaymentEntity> paymentEntityRowMapper() {
        return (rs, rowNum) -> new PaymentEntity(
                rs.getLong("id"),
                rs.getLong("member_id"),
                rs.getInt("original_price"),
                rs.getInt("discount_price"),
                rs.getInt("final_price"),
                rs.getTimestamp("created_at").toLocalDateTime());
    }
}
