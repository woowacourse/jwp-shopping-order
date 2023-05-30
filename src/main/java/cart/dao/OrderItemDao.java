package cart.dao;

import cart.entity.OrderItemEntity;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderItemDao {
	private static final RowMapper<OrderItemEntity> orderItemRowMapper = ((rs, rowNum) ->
		new OrderItemEntity(
			rs.getLong("id"),
			rs.getLong("order_id"),
			rs.getLong("product_id"),
			rs.getString("product_name"),
			rs.getInt("product_price"),
			rs.getInt("product_quantity"),
			rs.getString("product_image_url")
		));

	private final JdbcTemplate jdbcTemplate;

	public OrderItemDao(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<OrderItemEntity> findByOrderId(final Long orderId) {
		final String sql = "SELECT * FROM order_item WHERE order_id = ?";
		return jdbcTemplate.query(sql, orderItemRowMapper, orderId);
	}
}
