package cart.persistence.dao;

import cart.persistence.entity.OrderEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("purchase_order")
                .usingColumns("original_price", "discount_price", "used_coupon_id", "confirm_state", "member_id")
                .usingGeneratedKeyColumns("id");
    }

    public List<OrderEntity> findOrderByMemberId(Long memberId) {
        String sql = "SELECT * FROM purchase_order WHERE member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, new OrderEntityRowMapper());
    }

    public Optional<OrderEntity> findOrderById(Long id) {
        String sql = "SELECT * FROM purchase_order WHERE id = ?";
        List<OrderEntity> orders = jdbcTemplate.query(sql, new Object[]{id}, new OrderEntityRowMapper());
        return orders.isEmpty() ? Optional.empty() : Optional.ofNullable(orders.get(0));
    }

    public Long add(OrderEntity orderEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(orderEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public void delete(Long id) {
        String sql = "DELETE FROM purchase_order WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Long update(OrderEntity orderEntity) {
        String sql = "UPDATE purchase_order SET original_price = ?, discount_price = ?, used_coupon_id = ?, confirm_state = ?, member_id = ?  WHERE id = ?";
        jdbcTemplate.update(sql, orderEntity.getOriginalPrice(), orderEntity.getDiscountPrice(), orderEntity.getUsedCouponId(), orderEntity.getConfirmState(), orderEntity.getMemberId(), orderEntity.getId());
        return orderEntity.getId();
    }

    private static class OrderEntityRowMapper implements RowMapper<OrderEntity> {
        @Override
        public OrderEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrderEntity(
                    rs.getLong("id"),
                    rs.getInt("original_price"),
                    rs.getInt("discount_price"),
                    rs.getBoolean("confirm_state"),
                    rs.getLong("used_coupon_id"),
                    rs.getLong("member_id")
            );
        }
    }
}

