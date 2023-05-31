package cart.application.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.order.dto.OrderDetailDto;
import cart.application.order.dto.OrderDto;
import cart.application.order.dto.OrderItemDto;
import cart.domain.order.Order;
import cart.domain.order.OrderRepository;
import cart.domain.order.Orders;

@Transactional(readOnly = true)
@Service
public class OrderQueryService {

	private final OrderRepository orderRepository;

	public OrderQueryService(final OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<OrderDto> findByMemberId(final Long memberId) {
		final Orders orders = new Orders(memberId, orderRepository.findByMemberId(memberId));

		return orders.getOrders().stream()
			.map(this::convertToOrderDto)
			.collect(Collectors.toList());
	}

	public OrderDetailDto findById(final Long memberId, final Long id) {
		final Orders orders = new Orders(memberId, orderRepository.findByMemberId(memberId));

		return orders.getOrder(id)
			.map(this::convertToOrderDetailDto)
			.orElseThrow(() -> new IllegalArgumentException("해당 멤버의 주문이 아닙니다."));
	}

	private OrderDto convertToOrderDto(final Order order) {
		return new OrderDto(order.getId(), convertToOrderItems(order));
	}

	public OrderDetailDto convertToOrderDetailDto(final Order order) {
		return new OrderDetailDto(order.getId(), convertToOrderItems(order), order.calculateTotalProductPrice(),
			order.getDeliveryFee().getDeliveryFee());
	}

	private List<OrderItemDto> convertToOrderItems(final Order order) {
		return order.getOrderItems().stream()
			.map(OrderItemDto::from)
			.collect(Collectors.toList());
	}
}
