package cart.application.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.order.dto.OrderDetailResponse;
import cart.application.order.dto.OrderResponse;
import cart.domain.order.OrderRepository;
import cart.domain.order.Orders;

@Transactional(readOnly = true)
@Service
public class OrderQueryService {

	private final OrderRepository orderRepository;

	public OrderQueryService(final OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public List<OrderResponse> findByMemberId(final Long memberId) {
		final Orders orders = new Orders(memberId, orderRepository.findByMemberId(memberId));

		return orders.getOrders().stream()
			.map(OrderResponse::from)
			.collect(Collectors.toList());
	}

	public OrderDetailResponse findById(final Long memberId, final Long id) {
		final Orders orders = new Orders(memberId, orderRepository.findByMemberId(memberId));

		return orders.getOrder(id)
			.map(OrderDetailResponse::from)
			.orElseThrow(() -> new IllegalArgumentException("해당 멤버의 주문이 아닙니다."));
	}

}
