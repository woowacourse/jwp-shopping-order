package cart.integration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import cart.application.order.dto.OrderDetailResponse;
import cart.application.order.dto.OrderRequest;
import cart.application.order.dto.OrderResponse;
import cart.domain.member.Member;
import cart.domain.member.MemberRepository;

public class OrderIntegrationTest extends IntegrationTest {

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("유저의 주문 목록을 조회한다.")
	void getOrderListTest() {
		//given
		final Member member = memberRepository.findById(1L).get();

		//when
		final List<OrderResponse> orderResponses = get(member, "/orders")
			.jsonPath()
			.getList(".", OrderResponse.class);

		//then
		assertThat(orderResponses).hasSize(3);
		assertThat(orderResponses.get(0).getProducts()).hasSize(1);
		assertThat(orderResponses.get(1).getProducts()).hasSize(2);
		assertThat(orderResponses.get(2).getProducts()).hasSize(3);
	}

	@Test
	@DisplayName("특정 주문의 상세 정보를 조회할 때, 정상적인 요청이면 상태코드 200을 반환한다.")
	void getOrderDetailTest() {
		//given
		final Member member = memberRepository.findById(1L).get();
		final long orderId = 1L;

		//when
		final OrderDetailResponse orderDetailResponse = get(member, "/orders/{orderId}", orderId, HttpStatus.OK.value())
			.jsonPath()
			.getObject(".", OrderDetailResponse.class);

		//then
		assertThat(orderDetailResponse.getOrderId()).isEqualTo(orderId);
		assertThat(orderDetailResponse.getProducts()).hasSize(3);
		assertThat(orderDetailResponse.getTotalPrice()).isEqualTo(BigDecimal.valueOf(585400L));
	}

	@Test
	@DisplayName("특정 주문의 상세 정보를 조회할 때, 계정정보가 없으면 401을 반환한다.")
	void UnauthorizedExceptionTest() {
		//given
		final long orderId = 1L;

		//when
		given().log().all()
			.when()
			.get("/orders/{orderId}", orderId)
			.then()
			.log().all()
			.statusCode(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	@DisplayName("특정 주문의 상세 정보를 조회할 때, 주문 정보를 찾을 수 없으면 400을 반환한다.")
	void BadRequestTest() {
		//given
		final Member member = memberRepository.findById(2L).get();
		final long orderId = 1L;

		//when
		get(member, "/orders/{orderId}", orderId, HttpStatus.BAD_REQUEST.value());
	}

	@Test
	@DisplayName("장바구니에서 선택한 물건을 주문한다.")
	void placeOrderTest() {
		//given
		final Member member = memberRepository.findById(1L).get();
		final List<Long> cartItemIdList = List.of(1L, 2L);
		final BigDecimal totalPrice = BigDecimal.valueOf(380400L);
		final BigDecimal deliveryFee = BigDecimal.valueOf(3000L);

		//when
		final OrderRequest orderRequest = new OrderRequest(cartItemIdList, totalPrice, deliveryFee, null);

		final String location = post(member, orderRequest, "/orders", HttpStatus.CREATED.value())
			.header("Location");

		//then
		assertThat(location).isNotNull();
	}

	@Test
	@DisplayName("주문을 취소한다.")
	void cancelOrderTest() {
		//given
		final Member member = memberRepository.findById(1L).get();
		final long orderId = 3L;

		//when
		patch(member, "/orders/{orderId}", orderId, HttpStatus.OK.value());
	}

	@Test
	@DisplayName("주문 내역을 삭제한다.")
	void deleteOrderHistoryTest() {
		// given
		final Member member = memberRepository.findById(1L).get();
		final long orderId = 1L;

		// when & then
		delete(member, "/orders/{orderId}", orderId, HttpStatus.NO_CONTENT.value());

	}
}
