package cart.ui;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cart.application.order.OrderCommandService;
import cart.application.order.OrderQueryService;
import cart.application.order.dto.OrderDetailResponse;
import cart.application.order.dto.OrderRequest;
import cart.application.order.dto.OrderResponse;
import cart.config.auth.LoginMember;
import cart.config.auth.dto.AuthMember;

@CrossOrigin(origins = {"https://feb-dain.github.io", "https://cruelladevil.github.io", "http://localhost:3000"},
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
	public ResponseEntity<List<OrderResponse>> getOrderList(@LoginMember AuthMember member) {
		final List<OrderResponse> orderResponses = orderQueryService.findByMemberId(member.getId());

		return ResponseEntity.ok(orderResponses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderDetailResponse> getOrderDetails(@LoginMember AuthMember member, @PathVariable Long id) {
		final OrderDetailResponse orderDetailResponse = orderQueryService.findById(member.getId(), id);
		return ResponseEntity.ok(orderDetailResponse);
	}

	@PostMapping
	public ResponseEntity<Void> placeOrder(@LoginMember AuthMember member, @Valid @RequestBody OrderRequest request) {
		final Long savedId = orderCommandService.addOrder(member.getId(), request);
		return ResponseEntity.created(URI.create("/orders/" + savedId)).build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> cancelOrder(@LoginMember AuthMember member, @PathVariable Long id) {
		orderCommandService.cancelOrder(member.getId(), id);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrderHistory(@LoginMember AuthMember member, @PathVariable Long id) {
		orderCommandService.remove(member.getId(), id);
		return ResponseEntity.noContent().build();
	}

}
