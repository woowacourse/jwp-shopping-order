package cart.application.order.dto;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import cart.application.coupon.dto.CouponResponse;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;

public class OrderDetailResponse {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private Long orderId;
	private List<OrderItemResponse> products;
	private BigDecimal totalPrice;
	private BigDecimal totalPayments;
	private BigDecimal deliveryFee;
	private CouponResponse coupon;
	private String createdAt;
	private String orderStatus;

	public OrderDetailResponse() {
	}

	public static OrderDetailResponse from(final Order order) {
		final List<OrderItemResponse> products = convertToOrderItemResponses(order.getOrderItems());
		return new OrderDetailResponse(order.getId(), products, order.calculateTotalPrice(),
			order.calculateTotalPayments(), order.getDeliveryFee(),
			new CouponResponse(order.getCouponInfo()), order.getCreatedAt().format(formatter),
			order.getOrderStatus().getStatus());
	}

	public OrderDetailResponse(final Long orderId, final List<OrderItemResponse> products, final BigDecimal totalPrice,
		final BigDecimal totalPayments, final BigDecimal deliveryFee, final CouponResponse coupon,
		final String createdAt, final String orderStatus) {
		this.orderId = orderId;
		this.products = products;
		this.totalPrice = totalPrice;
		this.totalPayments = totalPayments;
		this.deliveryFee = deliveryFee;
		this.coupon = coupon;
		this.createdAt = createdAt;
		this.orderStatus = orderStatus;
	}

	public Long getOrderId() {
		return orderId;
	}

	public List<OrderItemResponse> getProducts() {
		return products;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public BigDecimal getTotalPayments() {
		return totalPayments;
	}

	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}

	public CouponResponse getCoupon() {
		return coupon;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	private static List<OrderItemResponse> convertToOrderItemResponses(final List<OrderItem> orderItems) {
		return orderItems.stream()
			.map(OrderItemResponse::from)
			.collect(Collectors.toList());
	}
}
