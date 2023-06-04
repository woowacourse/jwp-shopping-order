package cart.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import cart.domain.coupon.type.CouponInfo;
import cart.domain.monetary.DeliveryFee;

public class Order {

	private final Long id;
	private final CouponInfo couponInfo;
	private final DeliveryFee deliveryFee;
	private OrderStatus orderStatus;
	private final List<OrderItem> orderItems;
	private final LocalDateTime createdAt;

	public Order(final Long id, final List<OrderItem> orderItems, final CouponInfo couponInfo,
		final DeliveryFee deliveryFee, final OrderStatus orderStatus, final LocalDateTime createdAt) {
		this.id = id;
		this.orderItems = orderItems;
		this.couponInfo = couponInfo;
		this.deliveryFee = deliveryFee;
		this.orderStatus = orderStatus;
		this.createdAt = createdAt;
	}

	public boolean isSameTotalPrice(final BigDecimal totalPrice) {
		return calculateTotalPrice().equals(totalPrice);
	}

	public boolean isDifferentTotalPrice(final BigDecimal totalPrice) {
		return !isSameTotalPrice(totalPrice);
	}

	public BigDecimal calculateTotalPrice() {
		return orderItems.stream()
			.map(OrderItem::calculatePrice)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal calculateTotalPayments() {
		return couponInfo.calculatePayments(calculateTotalPrice()).add(deliveryFee.getAmount());
	}

	public Long getId() {
		return id;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public BigDecimal getDeliveryFee() {
		return deliveryFee.getAmount();
	}

	public CouponInfo getCouponInfo() {
		return couponInfo;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void updateOrderStatus(final OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
}
