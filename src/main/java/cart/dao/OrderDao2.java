package cart.dao;

import cart.dao.entity.OrderEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class OrderDao2 {
    private final JdbcTemplate jdbcTemplate;

    public OrderDao2(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) -> new OrderEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("member_coupon_id"),
            rs.getInt("shipping_fee"),
            rs.getInt("total_price"),
            new Date(rs.getDate("created_at").getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    );

    public Long save(final OrderEntity orderEntity) {
        final String sql = "INSERT INTO `order` (member_id, member_coupon_id, shipping_fee, total_price) VALUES(?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            ps.setLong(1, orderEntity.getMemberId());
            ps.setObject(2, orderEntity.getMemberCouponId(), Types.BIGINT);
            ps.setInt(3, orderEntity.getShippingFee());
            ps.setInt(4, orderEntity.getTotalPrice());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<OrderEntity> findByMemberId(final Long memberId) {
        final String sql = "SELECT * FROM `order` WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Optional<OrderEntity> findById(final Long id) {
        final String sql = "SELECT * FROM `order` WHERE id = ?";
        try {
            OrderEntity orderEntity = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(orderEntity);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM order_item WHERE id = ? ";
        jdbcTemplate.update(sql, id);
    }
}
