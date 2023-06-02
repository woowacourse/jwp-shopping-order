package cart.dao;

import cart.domain.Payment;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentDao {
    private static final RowMapper<Payment> mapper = (rs, rowNum) -> {
        int total_product_price = rs.getInt("total_product_price");
        int total_delivery_price = rs.getInt("total_delivery_price");
        int use_point = rs.getInt("use_point");
        int total_price = rs.getInt("total_price");
        return new Payment(total_product_price, total_delivery_price, use_point, total_price);
    };

    private final JdbcTemplate jdbcTemplate;

    public PaymentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long orderId, Payment payment) {
        String sql = "INSERT INTO payment (orders_id, total_product_price, total_delivery_price, use_point, total_price) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderId, payment.getTotalProductPrice(), payment.getTotalDeliveryFee(),
                payment.getUsePoint(), payment.getTotalPrice());
    }

    public Optional<Payment> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM payment WHERE orders_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, mapper, orderId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
