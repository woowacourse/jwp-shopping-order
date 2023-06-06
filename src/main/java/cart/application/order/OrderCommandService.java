package cart.application.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.order.dto.OrderRequest;
import cart.domain.cart.Cart;
import cart.domain.cart.CartItemRepository;
import cart.domain.cart.CartRepository;
import cart.domain.coupon.MemberCoupon;
import cart.domain.coupon.MemberCouponRepository;
import cart.domain.coupon.type.CouponInfo;
import cart.domain.coupon.type.NotUsed;
import cart.domain.monetary.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderRepository;
import cart.domain.order.OrderStatus;
import cart.domain.order.Orders;
import cart.error.exception.BadRequestException;
import cart.error.exception.ConflictException;
import cart.error.exception.ForbiddenException;

@Transactional
@Service
public class OrderCommandService {

	private final OrderRepository orderRepository;
	private final MemberCouponRepository memberCouponRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;

	public OrderCommandService(final OrderRepository orderRepository,
		final MemberCouponRepository memberCouponRepository,
		final CartRepository cartRepository, final CartItemRepository cartItemRepository) {
		this.orderRepository = orderRepository;
		this.memberCouponRepository = memberCouponRepository;
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
	}

	public Long addOrder(final Long memberId, final OrderRequest request) {
		final Order order = createOrder(memberId, request);
		validateTotalPriceOfOrder(request, order);

		final Long savedId = orderRepository.save(memberId, order);
		cartItemRepository.deleteByIds(request.getCartItemIds());

		return savedId;
	}

	private Order createOrder(final Long memberId, final OrderRequest request) {
		final Cart cart = cartRepository.findByMemberId(memberId);
		final List<OrderItem> orderItems = createOrderItems(request.getCartItemIds(), cart);
		final DeliveryFee deliveryFee = new DeliveryFee(request.getDeliveryFee());

		if (request.getCouponId() != null) {
			final CouponInfo coupon = findCoupon(memberId, request);
			return new Order(orderItems, coupon, deliveryFee, OrderStatus.PAID);
		}

		return new Order(orderItems, new NotUsed(), deliveryFee, OrderStatus.PAID);
	}

	private List<OrderItem> createOrderItems(final List<Long> cartItemIds, final Cart cart) {
		return cartItemIds.stream()
			.map(cart::getCartItem)
			.map(optionalCartItem -> optionalCartItem.orElseThrow(BadRequestException.Cart::new))
			.map(OrderItem::new)
			.collect(Collectors.toList());
	}

	private CouponInfo findCoupon(final Long memberId, final OrderRequest request) {
		final MemberCoupon memberCoupon = memberCouponRepository.findByMemberId(memberId);
		final CouponInfo couponInfo = memberCoupon.getCouponInfo(request.getCouponId())
			.orElseThrow(BadRequestException.Coupon::new);
		memberCouponRepository.deleteByMemberIdAndCouponId(memberId, couponInfo.getId());
		return couponInfo;
	}

	private void validateTotalPriceOfOrder(final OrderRequest request, final Order order) {
		if (order.isDifferentTotalPrice(request.getTotalPrice())) {
			throw new ConflictException.Monetary();
		}
	}

	public void cancelOrder(final Long memberId, final Long orderId) {
		final Orders orders = new Orders(memberId, orderRepository.findByMemberId(memberId));
		final Order order = orders.getOrder(orderId)
			.orElseThrow(ForbiddenException.Order::new);

		validateOrderStatus(order);

		order.updateOrderStatus(OrderStatus.CANCELED);
		orderRepository.updateStatus(order);

	}

	private void validateOrderStatus(final Order order) {
		if (order.getOrderStatus() != OrderStatus.PAID) {
			throw new BadRequestException.OrderStatusUpdate();
		}
	}

	public void remove(final Long memberId, final Long orderId) {
		final Orders orders = new Orders(memberId, orderRepository.findByMemberId(memberId));
		if (orders.contain(orderId)) {
			orderRepository.deleteById(orderId);
			return;
		}
		throw new ForbiddenException.Order();
	}
}
