package cart.persistence.order;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class OrderJoinItemRowMapper implements RowMapper<OrderJoinItem> {

	@Override
	public OrderJoinItem mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		return new OrderJoinItem(
			rs.getLong("orders.id"),
			rs.getLong("orders.delivery_fee"),
			rs.getLong("order_item.id"),
			rs.getString("order_item.name"),
			rs.getLong("order_item.price"),
			rs.getString("order_item.image_url"),
			rs.getInt("order_item.quantity")
		);
	}
}
