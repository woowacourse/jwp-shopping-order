package cart.domain.order;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Orders {

	private final Long memberId;
	private final Map<Long, Order> orders;

	public Orders(final Long memberId, final List<Order> orders) {
		this.memberId = memberId;
		this.orders = ordersToMapById(orders);
	}

	public boolean contain(final Long orderId) {
		return orders.containsKey(orderId);
	}

	public Optional<Order> getOrder(final Long orderId) {
		return Optional.ofNullable(orders.get(orderId));
	}

	public Long getMemberId() {
		return memberId;
	}

	public List<Order> getOrders() {
		return orders.values().stream()
			.sorted(Comparator.comparing(Order::getId).reversed())
			.collect(Collectors.toList());
	}

	private static Map<Long, Order> ordersToMapById(final List<Order> orders) {
		return orders.stream()
			.collect(Collectors.toMap(Order::getId, Function.identity()));
	}

}
