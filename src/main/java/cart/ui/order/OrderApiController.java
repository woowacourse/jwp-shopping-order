package cart.ui.order;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cart.application.order.OrderCommandService;
import cart.application.order.OrderQueryService;
import cart.application.order.dto.OrderAddDto;
import cart.application.order.dto.OrderDetailDto;
import cart.application.order.dto.OrderDto;
import cart.application.order.dto.OrderItemDto;
import cart.domain.member.Member;
import cart.ui.order.dto.OrderDetailResponse;
import cart.ui.order.dto.OrderItemResponse;
import cart.ui.order.dto.OrderRequest;
import cart.ui.order.dto.OrderResponse;

@CrossOrigin(origins = {"https://feb-dain.github.io", "http://localhost:3000"},
	allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH,
	RequestMethod.DELETE, RequestMethod.OPTIONS},
	allowCredentials = "true", exposedHeaders = "Location")
@RestController
@RequestMapping("/orders")
public class OrderApiController {

	private final OrderCommandService orderCommandService;
	private final OrderQueryService orderQueryService;

	public OrderApiController(final OrderCommandService orderCommandService,
		final OrderQueryService orderQueryService) {
		this.orderCommandService = orderCommandService;
		this.orderQueryService = orderQueryService;
	}

	@GetMapping
	public ResponseEntity<List<OrderResponse>> getOrderList(Member member) {
		final List<OrderResponse> orderResponses = orderQueryService.findByMemberId(member.getId()).stream()
			.map(this::convertToOrderResponse)
			.collect(Collectors.toList());

		return ResponseEntity.ok(orderResponses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderDetailResponse> getOrderDetails(Member member, @PathVariable Long id) {
		final OrderDetailResponse orderDetailResponse = convertToOrderDetailResponse(
			orderQueryService.findById(member.getId(), id));
		return ResponseEntity.ok(orderDetailResponse);
	}

	@PostMapping
	public ResponseEntity<Void> placeOrder(Member member, @Valid @RequestBody OrderRequest request) {
		final Long savedId = orderCommandService.addOrder(new OrderAddDto(member.getId(), request));
		return ResponseEntity.created(URI.create("/orders/" + savedId)).build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> cancelOrder(Member member, @PathVariable Long id) {
		orderCommandService.remove(member.getId(), id);
		return ResponseEntity.noContent().build();
	}

	private OrderResponse convertToOrderResponse(final OrderDto orderDto) {
		return new OrderResponse(orderDto.getOrderId(), convertToOrderItemResponses(orderDto.getOrderItemDtos()));
	}

	private OrderDetailResponse convertToOrderDetailResponse(final OrderDetailDto orderDetailDto) {
		return new OrderDetailResponse(
			orderDetailDto.getOrderId(),
			convertToOrderItemResponses(orderDetailDto.getOrderItemDtos()),
			orderDetailDto.getTotalPrice(),
			orderDetailDto.getDeliveryFee());
	}

	private List<OrderItemResponse> convertToOrderItemResponses(final List<OrderItemDto> orderItemDtos) {
		return orderItemDtos.stream()
			.map(OrderItemResponse::new)
			.collect(Collectors.toList());
	}
}
