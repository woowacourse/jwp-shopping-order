package cart.persistence.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cart.domain.coupon.type.CouponInfo;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderStatus;
import cart.domain.product.Product;

public class OrderJoinItem {

	private Long orderId;
	private BigDecimal deliveryFee;
	private OrderItem orderItem;
	private CouponInfo couponInfo;
	private OrderStatus orderStatus;
	private LocalDateTime createdAt;

	public OrderJoinItem(final Long orderId, final BigDecimal deliveryFee,
		final Long orderItemId, final String itemName,
		final BigDecimal itemPrice, final String itemImageUrl, final int quantity,
		final Long couponId, final String couponName, final String couponType, final BigDecimal discount,
		final OrderStatus orderStatus, final LocalDateTime createdAt) {
		this.orderId = orderId;
		this.deliveryFee = deliveryFee;
		this.createdAt = createdAt;
		this.orderItem = new OrderItem(orderItemId, new Product(itemName, itemPrice, itemImageUrl), quantity);
		this.couponInfo = CouponInfo.of(couponId, couponType, couponName, discount);
		this.orderStatus = orderStatus;
	}

	public Long getOrderId() {
		return orderId;
	}

	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}

	public OrderItem getOrderItem() {
		return orderItem;
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
}
