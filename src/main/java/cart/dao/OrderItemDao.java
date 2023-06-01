package cart.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import cart.dao.dto.OrderItemDto;
import cart.domain.MemberCoupon;
import cart.domain.OrderItem;

@Repository
public class OrderItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final SimpleJdbcInsert orderItemMemberCouponInsert;

    public OrderItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item")
                .usingGeneratedKeyColumns("id");
        this.orderItemMemberCouponInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_item_member_coupon")
                .usingGeneratedKeyColumns("id");
    }

    public void insertAll(Long orderId, List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            Long orderItemId = insert(orderId, orderItem);
            insertMemberCouponIds(orderItemId, orderItem.getUsedCoupons());
        }
    }

    private Long insert(Long orderId, OrderItem orderItem) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("order_id", orderId)
                .addValue("product_id", orderItem.getOrderedProduct().getId())
                .addValue("name", orderItem.getOrderedProduct().getName())
                .addValue("price", orderItem.getOrderedProduct().getPrice().getValue())
                .addValue("image_url", orderItem.getOrderedProduct().getImageUrl())
                .addValue("quantity", orderItem.getQuantity());

        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    private void insertMemberCouponIds(Long orderItemId, List<MemberCoupon> memberCoupons) {
        SqlParameterSource[] parameterSources = memberCoupons.stream()
                .map(memberCoupon -> new MapSqlParameterSource()
                        .addValue("order_item_id", orderItemId)
                        .addValue("member_coupon_id", memberCoupon.getId())
                )
                .toArray(SqlParameterSource[]::new);

        orderItemMemberCouponInsert.executeBatch(parameterSources);
    }

    public List<OrderItemDto> selectAllOf(Long orderId) {
        String sql = "select * from order_item where order_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long orderItemId = rs.getLong("id");
            return new OrderItemDto(
                    orderItemId,
                    rs.getLong("product_id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url"),
                    rs.getInt("quantity"),
                    selectAllMemberCouponIdsOf(orderItemId)
            );
        }, orderId);
    }

    private List<Long> selectAllMemberCouponIdsOf(Long orderItemId) {
        String sql = "select member_coupon_id from order_item_member_coupon where order_item_id = ?";

        return jdbcTemplate.queryForList(sql, Long.class, orderItemId);
    }
}
