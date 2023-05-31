package cart.application.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.order.dto.OrderAddDto;
import cart.domain.cart.Cart;
import cart.domain.cart.CartItemRepository;
import cart.domain.cart.CartRepository;
import cart.domain.order.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderRepository;
import cart.domain.order.Orders;
import cart.error.exception.OrderException;

@Transactional
@Service
public class OrderCommandService {

	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;
	private final CartItemRepository cartItemRepository;

	public OrderCommandService(final CartRepository cartRepository, final OrderRepository orderRepository,
		final CartItemRepository cartItemRepository) {
		this.cartRepository = cartRepository;
		this.orderRepository = orderRepository;
		this.cartItemRepository = cartItemRepository;
	}

	public Long addOrder(final OrderAddDto orderAddDto) {
		final Order order = createOrder(orderAddDto);
		validateTotalPriceOfOrder(orderAddDto, order);

		final Long savedId = orderRepository.save(orderAddDto.getMemberId(), order);
		cartItemRepository.deleteByIds(orderAddDto.getCartItemIds());

		return savedId;
	}

	public void remove(final Long memberId, final Long orderId) {
		final Orders orders = new Orders(memberId, orderRepository.findByMemberId(memberId));
		if (orders.contain(orderId)) {
			orderRepository.deleteById(orderId);
			return;
		}
		throw new OrderException.NotFound();
	}

	private void validateTotalPriceOfOrder(final OrderAddDto orderAddDto, final Order order) {
		if (order.isDifferentTotalPrice(orderAddDto.getTotalPrice())) {
			throw new OrderException.BadRequest("주문 가격이 잘못되었습니다.");
		}
	}

	private Order createOrder(final OrderAddDto orderAddDto) {
		final Cart cart = cartRepository.findByMemberId(orderAddDto.getMemberId());
		final List<OrderItem> orderItems = createOrderItems(orderAddDto.getCartItemIds(), cart);

		return new Order(null, orderItems, new DeliveryFee(orderAddDto.getDeliveryFee()));
	}

	private List<OrderItem> createOrderItems(final List<Long> cartItemIds, final Cart cart) {
		return cartItemIds.stream()
			.map(cart::getCartItem)
			.map(optionalCartItem -> optionalCartItem.orElseThrow(
				() -> new IllegalArgumentException("카트에 해당 물품이 존재하지 않습니다.")))
			.map(OrderItem::new)
			.collect(Collectors.toList());
	}
}
