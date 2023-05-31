package cart.dao;

import cart.domain.Member;
import cart.domain.Payment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentDao {
    private final JdbcTemplate jdbcTemplate;

    public PaymentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long orderId, Payment payment) {
        String sql = "INSERT INTO payment (orders_id, total_product_price, total_delivery_price, use_point, total_price) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderId, payment.getTotalProductPrice(), payment.getTotalDeliveryFee(), payment.getUsePoint(), payment.getTotalPrice());
    }
}
