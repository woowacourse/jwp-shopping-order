package cart.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import cart.domain.Member;
import cart.dto.CartItemRequest;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderItemResponse;
import cart.dto.OrderResponse;
import cart.exception.InvalidProductException;
import cart.exception.InvalidQuantityException;

class OrderServiceTest extends ServiceTest {

	@Test
	void 주문시_포인트_사용을_확인한다() {
		// given
		final Member member = new Member(1L, EMAIL, PASSWORD, 1000);

		// when
		final Long orderId = orderService.createOrder(ORDER_CREATE_REQUEST, member);
		final OrderResponse response = orderService.findById(orderId, member);
		final Member foundMember = memberDao.getMemberById(1L);

		assertAll(
			() -> assertThat(foundMember.getPoints()).isEqualTo(10780),
			() -> assertThat(response.getPoints()).isEqualTo(200)
		);
	}

	@Test
	void 주문을_생성한다() {
		// given
		final Member member = new Member(1L, EMAIL, PASSWORD, 1000);

		// when
		final Long orderId = orderService.createOrder(ORDER_CREATE_REQUEST, member);

		// then
		assertThat(orderId).isEqualTo(2);
	}

	@Test
	void 보유한_포인트보다_많은_포인트를_사용할_경우_예외가_발생한다() {
		// given
		final Member member = new Member(1L, EMAIL, PASSWORD, 1000);
		final OrderCreateRequest request = new OrderCreateRequest(2000, List.of(CART_ITEM_REQUEST_1,
			CART_ITEM_REQUEST_2));

		// then
		assertThatThrownBy(() -> orderService.createOrder(request, member))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 주문한_상품이_장바구니의_상품과_일치하지_않으면_예외가_발생한다() {
		// given
		final Member member = new Member(1L, EMAIL, PASSWORD, 1000);
		final OrderCreateRequest request = new OrderCreateRequest(200, List.of(
			new CartItemRequest(1L, 3L, 2),
			new CartItemRequest(2L, 4L, 4)
		));

		// then
		assertThatThrownBy(() -> orderService.createOrder(request, member))
			.isInstanceOf(InvalidProductException.class)
			.hasMessage("장바구니에 등록한 상품과 일치하지 않습니다");
	}

	@Test
	void 주문한_상품_수량이_장바구니의_상품_수량과_일치하지_않으면_예외가_발생한다() {
		// given
		final Member member = new Member(1L, EMAIL, PASSWORD, 1000);
		final OrderCreateRequest request = new OrderCreateRequest(200, List.of(
			new CartItemRequest(1L, 1L, 4),
			new CartItemRequest(2L, 2L, 8)
		));

		// then
		assertThatThrownBy(() -> orderService.createOrder(request, member))
			.isInstanceOf(InvalidQuantityException.class)
			.hasMessage("장바구에 등록한 상품 수량과 일치하지 않습니다");
	}

	@Test
	void 단일_주문_이력을_조회한다() {
		// given
		final Long orderId = 1L;
		final Member member = new Member(1L, EMAIL, PASSWORD, 1000);
		final List<OrderItemResponse> expected = List.of(
			new OrderItemResponse(1L, "치킨", 10000, 1,
				"https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80")
		);

		// when
		final OrderResponse orderResponse = orderService.findById(orderId, member);

		// then
		assertAll(
			() -> assertThat(orderResponse.getCartItems()).usingRecursiveComparison().isEqualTo(expected),
			() -> assertThat(orderResponse.getPoints()).isEqualTo(1000)
		);
	}

	@Test
	void 전체_주문_이력을_조회한다() {
		// given
		final Member member = new Member(1L, EMAIL, PASSWORD, 1000);
		final List<OrderItemResponse> orderItemResponses = List.of(
			new OrderItemResponse(1L, "치킨", 10000, 1,
				"https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80")
		);
		List<OrderResponse> expected = List.of(
			new OrderResponse(1L, 10, 1000, orderItemResponses)
		);

		// when
		final List<OrderResponse> orderResponses = orderService.findAll(member);

		// then
		assertThat(orderResponses)
			.usingRecursiveComparison()
			.isEqualTo(expected);
	}
}
