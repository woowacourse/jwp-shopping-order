package cart.persistence.dao;

import cart.persistence.dto.OrderDetailDTO;
import cart.persistence.entity.OrderEntity;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Optional;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long create(final OrderEntity order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO `order` (member_id, member_coupon_id, shipping_fee, total_price) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, order.getMemberId());
            ps.setObject(2, Optional.ofNullable(order.getMemberCouponId()).orElse(null), Types.BIGINT);
            ps.setInt(3, order.getShippingFee());
            ps.setInt(4, order.getTotalPrice());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<OrderDetailDTO> findById(final long id) {
        String sql = "SELECT * FROM `order` "
                + "INNER JOIN member ON `order`.member_id = member.id "
                + "INNER JOIN member_coupon ON `order`.member_coupon_id = member_coupon.id "
                + "WHERE `order`.id = ?";
        try {
            OrderDetailDTO order = jdbcTemplate.queryForObject(sql, RowMapperHelper.orderDetailRowMapper(),
                    id);
            return Optional.of(order);
        } catch (IncorrectResultSizeDataAccessException exception) {
            return Optional.empty();
        }
    }
}
