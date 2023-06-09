package cart.persistence.order;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cart.domain.order.OrderStatus;

public class OrderJoinItemRowMapper implements RowMapper<OrderJoinItem> {

	@Override
	public OrderJoinItem mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		return new OrderJoinItem(
			rs.getLong("orders.id"),
			rs.getBigDecimal("orders.delivery_fee"),
			rs.getLong("order_item.id"),
			rs.getString("order_item.name"),
			rs.getBigDecimal("order_item.price"),
			rs.getString("order_item.image_url"),
			rs.getInt("order_item.quantity"),
			rs.getLong("orders.coupon_id"),
			rs.getString("coupon.name"),
			rs.getString("coupon.discount_type"),
			rs.getBigDecimal("coupon.discount"),
			OrderStatus.valueOf(rs.getString("orders.order_status")),
			rs.getTimestamp("orders.created_at").toLocalDateTime()
		);
	}
}
