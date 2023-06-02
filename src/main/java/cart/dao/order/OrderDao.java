package cart.dao.order;

import cart.domain.order.Order;
import cart.entity.OrderEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) -> new OrderEntity(
            rs.getLong("id"),
            rs.getInt("total_item_price"),
            rs.getInt("discounted_total_item_price"),
            rs.getInt("shipping_fee"),
            rs.getTimestamp("ordered_at").toLocalDateTime(),
            rs.getLong("member_id")
    );

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Long insertOrder(final Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO orders (total_item_price, discounted_total_item_price, shipping_fee, ordered_at, member_id) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, order.getPurchaseItemPrice());
            ps.setLong(2, order.getDiscountPurchaseItemPrice());
            ps.setInt(3, order.getShippingFee());
            ps.setDate(4, Date.valueOf(order.getGenerateTime().toLocalDate()));
            ps.setLong(5, order.getMember().getId());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<OrderEntity> findByMemberId(final Long memberId) {
        String sql = "SELECT id, total_item_price, discounted_total_item_price, shipping_fee, ordered_at, member_id FROM orders " +
                "WHERE member_id = ?";

        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Optional<OrderEntity> findById(final Long id) {
        String sql = "SELECT id, total_item_price, discounted_total_item_price, shipping_fee, ordered_at, member_id FROM orders " +
                "WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
