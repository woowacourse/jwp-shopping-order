package cart.persistence.order;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cart.domain.coupon.type.CouponInfo;
import cart.domain.monetary.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderRepository;
import cart.domain.order.OrderStatus;
import cart.domain.product.Product;
import cart.error.exception.ForbiddenException;

@Transactional
@Repository
public class OrderJdbcRepository implements OrderRepository {

	public static final RowMapper<OrderJoinItem> ORDER_JOIN_ITEM_ROW_MAPPER = new OrderJoinItemRowMapper();

	private final JdbcTemplate jdbcTemplate;

	public OrderJdbcRepository(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Long save(final Long memberId, final Order order) {
		Long orderId = insertOrder(memberId, order);
		insertOrderItems(order.getOrderItems(), orderId);

		return orderId;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Order> findByMemberId(final Long memberId) {
		final String sql = createBaseOrderQuery("WHERE orders.member_id = ?");
		final List<OrderJoinItem> orderJoinItems = jdbcTemplate.query(sql, ORDER_JOIN_ITEM_ROW_MAPPER, memberId);

		validateOrdersExist(orderJoinItems);

		return convertToOrders(groupOrderItemsByOrderId(orderJoinItems));
	}

	@Transactional(readOnly = true)
	@Override
	public Order findById(final Long id) {
		final String sql = createBaseOrderQuery("WHERE orders.id = ?");
		final List<OrderJoinItem> orderJoinItems = jdbcTemplate.query(sql, ORDER_JOIN_ITEM_ROW_MAPPER, id);

		validateOrdersExist(orderJoinItems);

		return convertToOrder(id, orderJoinItems);
	}

	@Override
	public void updateStatus(final Order order) {
		final String sql = "UPDATE orders SET order_status = ? WHERE id = ? ";
		jdbcTemplate.update(sql, order.getOrderStatus().name(), order.getId());

	}

	@Override
	public void deleteById(final Long id) {
		final String itemSql = "DELETE FROM order_item WHERE order_id = ?";
		jdbcTemplate.update(itemSql, id);
		final String orderSql = "DELETE FROM orders WHERE id = ?";
		jdbcTemplate.update(orderSql, id);
	}

	private Long insertOrder(final Long memberId, final Order order) {
		final String orderSql = "INSERT INTO orders (member_id, coupon_id, delivery_fee) VALUES (?, ?, ?)";

		final KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(orderSql, new String[] {"ID"});
			ps.setLong(1, memberId);
			ps.setLong(2, order.getCouponInfo().getId());
			ps.setBigDecimal(3, order.getDeliveryFee());
			return ps;
		}, keyHolder);

		return Objects.requireNonNull(keyHolder.getKey()).longValue();
	}

	private void insertOrderItems(final List<OrderItem> orderItems, final Long orderId) {
		final String sql = "INSERT INTO order_item (order_id, name, price, image_url, quantity) VALUES (?, ?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql, orderItems, orderItems.size(), (ps, orderItem) -> {
			final Product product = orderItem.getProduct();
			ps.setLong(1, orderId);
			ps.setString(2, product.getName());
			ps.setBigDecimal(3, product.getPrice());
			ps.setString(4, product.getImageUrl());
			ps.setInt(5, orderItem.getQuantity());
		});
	}

	private String createBaseOrderQuery(final String condition) {
		final String sql =
			"SELECT orders.id, orders.delivery_fee, "
				+ "order_item.id, order_item.name, order_item.price, order_item.image_url, order_item.quantity, "
				+ "orders.coupon_id, coupon.name, coupon.discount_type, coupon.discount, "
				+ "orders.order_status, orders.created_at "
				+ "FROM orders "
				+ "JOIN order_item ON orders.id = order_item.order_id "
				+ "LEFT JOIN coupon ON orders.coupon_id = coupon.id "
				+ "%s";

		return String.format(sql, condition);
	}

	private void validateOrdersExist(final List<OrderJoinItem> orderJoinItems) {
		if (orderJoinItems.isEmpty()) {
			throw new ForbiddenException.Order();
		}
	}

	private Map<Long, List<OrderJoinItem>> groupOrderItemsByOrderId(final List<OrderJoinItem> orderJoinItems) {
		return orderJoinItems.stream()
			.collect(Collectors.groupingBy(OrderJoinItem::getOrderId));
	}

	private List<Order> convertToOrders(final Map<Long, List<OrderJoinItem>> groupedOrderJoinItems) {
		return groupedOrderJoinItems.keySet().stream()
			.map(id -> convertToOrder(id, groupedOrderJoinItems.get(id)))
			.collect(Collectors.toList());
	}

	private Order convertToOrder(final Long id, final List<OrderJoinItem> orderJoinItems) {
		final List<OrderItem> orderItems = convertToOrderItem(orderJoinItems);
		final DeliveryFee deliveryFee = new DeliveryFee(orderJoinItems.get(0).getDeliveryFee());
		final CouponInfo couponInfo = orderJoinItems.get(0).getCouponInfo();
		final OrderStatus orderStatus = orderJoinItems.get(0).getOrderStatus();
		final LocalDateTime createdAt = orderJoinItems.get(0).getCreatedAt();

		return new Order(id, orderItems, couponInfo, deliveryFee, orderStatus, createdAt);
	}

	private List<OrderItem> convertToOrderItem(final List<OrderJoinItem> orderJoinItems) {
		return orderJoinItems.stream()
			.map(OrderJoinItem::getOrderItem)
			.collect(Collectors.toList());
	}
}
