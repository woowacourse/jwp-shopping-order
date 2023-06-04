package cart.application.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;

import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderStatus;

public class OrderResponse {

	private Long orderId;
	private List<OrderItemResponse> products;
	private BigDecimal totalPayments;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime createdAt;
	private String orderStatus;

	public OrderResponse() {
	}

	public OrderResponse(final Long orderId, final List<OrderItemResponse> products, final BigDecimal totalPayments,
		final LocalDateTime createdAt, final OrderStatus orderStatus) {
		this.orderId = orderId;
		this.products = products;
		this.totalPayments = totalPayments;
		this.createdAt = createdAt;
		this.orderStatus = orderStatus.getStatus();
	}

	public static OrderResponse from(final Order order) {
		final List<OrderItemResponse> products = convertToOrderItemResponses(order.getOrderItems());
		return new OrderResponse(order.getId(), products, order.calculateTotalPayments(), order.getCreatedAt(),
			order.getOrderStatus());
	}

	public Long getOrderId() {
		return orderId;
	}

	public List<OrderItemResponse> getProducts() {
		return products;
	}

	public BigDecimal getTotalPayments() {
		return totalPayments;
	}

	public LocalDateTime getCreatedAt() {
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
