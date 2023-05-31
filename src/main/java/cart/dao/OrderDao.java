package cart.dao;

import cart.domain.Order;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {
    
    private final JdbcTemplate jdbcTemplate;
    
    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    
    public Long save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO orders (member_id, product_price, discount_price, delivery_fee, total_price, created_at) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, order.getMemberId());
            ps.setInt(2, order.getProductPrice());
            ps.setInt(3, order.getDiscountPrice());
            ps.setInt(4, order.getDeliveryFee());
            ps.setInt(5, order.getTotalPrice());
            ps.setObject(6, order.getCreatedAt(), Types.TIMESTAMP); //todo : 자바 객체 뭐로 해야하는지 고민하기
            return ps;
        }, keyHolder);
        
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
