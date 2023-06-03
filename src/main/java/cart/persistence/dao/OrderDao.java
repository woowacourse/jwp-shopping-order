package cart.persistence.dao;

import cart.persistence.dao.dto.OrderDto;
import cart.persistence.entity.OrderEntity;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final RowMapper<OrderDto> orderDtoRowMapper = (rs, count) -> new OrderDto(
        rs.getLong("orderId"),
        rs.getTimestamp("orderedAt").toLocalDateTime(),
        rs.getInt("orderQuantity"),
        rs.getInt("totalPrice"),
        rs.getInt("discountedTotalPrice"),
        rs.getInt("deliveryPrice"),
        rs.getBoolean("is_valid"),
        rs.getLong("couponId"),
        rs.getString("couponName"),
        rs.getInt("couponDiscountRate"),
        rs.getInt("couponPeriod"),
        rs.getTimestamp("couponExpiredAt"),
        rs.getLong("memberId"),
        rs.getString("memberName"),
        rs.getString("memberPassword"),
        rs.getLong("productId"),
        rs.getString("productName"),
        rs.getInt("orderedProductPrice"),
        rs.getString("productImageUrl"),
        rs.getBoolean("productIsDeleted")
    );

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(final OrderEntity orderEntity) {
        final String query = "INSERT INTO `order`(member_id, total_price, discounted_total_price, "
            + "delivery_price, ordered_at, is_valid) VALUES (?, ?, ?, ?, ?, ?)";

        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setLong(1, orderEntity.getMemberId());
            ps.setInt(2, orderEntity.getTotalPrice());
            ps.setInt(3, orderEntity.getDiscountedTotalPrice());
            ps.setInt(4, orderEntity.getDeliveryPrice());
            ps.setTimestamp(5, Timestamp.valueOf(orderEntity.getOrderedAt()));
            ps.setBoolean(6, true);
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Long countByMemberId(final Long memberId) {
        final String sql = "SELECT COUNT(*) FROM `order` WHERE member_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, memberId);
    }

    public List<OrderDto> findById(final Long id) {
        final String sql = "SELECT m.id AS memberId, m.name AS memberName, m.password AS memberPassword, "
            + "o.id AS orderId, o.total_price AS totalPrice, o.discounted_total_price AS discountedTotalPrice, "
            + "o.delivery_price AS deliveryPrice, o.ordered_at AS orderedAt, o.is_valid As isValid, "
            + "c.id AS couponId, c.name AS couponName, c.discount_rate AS couponDiscountRate, "
            + "c.period AS couponPeriod, c.expired_at AS couponExpiredAt, "
            + "op.ordered_product_price AS orderedProductPrice, op.quantity as orderQuantity, "
            + "p.id AS productId, p.name AS productName, p.image_url AS productImageUrl, p.is_deleted As productIsDeleted "
            + "FROM `order` o "
            + "LEFT JOIN member m on o.member_id = m.id "
            + "LEFT JOIN order_coupon oc on o.id = oc.order_id "
            + "LEFT JOIN coupon c on c.id = oc.coupon_id "
            + "LEFT JOIN order_product op on o.id = op.order_id "
            + "LEFT JOIN product p on op.product_id = p.id "
            + "WHERE o.id = ?";
        return jdbcTemplate.query(sql, orderDtoRowMapper, id);
    }

    public List<OrderDto> findByMemberName(final String memberName) {
        final String sql = "SELECT m.id AS memberId, m.name AS memberName, m.password AS memberPassword, "
            + "o.id AS orderId, o.total_price AS totalPrice, o.discounted_total_price AS discountedTotalPrice, "
            + "o.delivery_price AS deliveryPrice, o.ordered_at AS orderedAt, o.is_valid AS isValid, "
            + "c.id AS couponId, c.name AS couponName, c.discount_rate AS couponDiscountRate, "
            + "c.period AS couponPeriod, c.expired_at AS couponExpiredAt, "
            + "op.ordered_product_price AS orderedProductPrice, op.quantity as orderQuantity, "
            + "p.id AS productId, p.name AS productName, p.image_url AS productImageUrl, p.is_deleted As productIsDeleted "
            + "FROM `order` o "
            + "LEFT JOIN member m on o.member_id = m.id "
            + "LEFT JOIN order_coupon oc on o.id = oc.order_id "
            + "LEFT JOIN coupon c on c.id = oc.coupon_id "
            + "LEFT JOIN order_product op on o.id = op.order_id "
            + "LEFT JOIN product p on op.product_id = p.id "
            + "WHERE m.name = ?";
        return jdbcTemplate.query(sql, orderDtoRowMapper, memberName);
    }

    public int updateNotValidById(final Long id) {
        final String sql = "UPDATE `order` SET is_valid = 0 WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
