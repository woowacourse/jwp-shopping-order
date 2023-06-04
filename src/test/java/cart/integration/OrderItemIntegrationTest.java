package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderRequest;
import cart.repository.CartItemRepository;
import cart.repository.MemberRepository;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderItemIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    private Member member;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberRepository.findById(1L);
    }

    @DisplayName("장바구니의 모든 아이템을 주문한다.")
    @Test
    void addCartItem() {
        // given
        // memberId = 1L인 사용자의 장바구니 안에 들어있는 아이템 : {'치킨', 10000원, 2개}, {'샐러드', 20000, 4개}
        List<CartItem> cartItems = cartItemRepository.findByMemberId(member.getId());
        List<Long> cartItemIds = cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());

        OrderRequest orderRequest = new OrderRequest(
                cartItemIds,
                "1234-1234-1234-1234",
                123,
                300
        );

        // when
        ExtractableResponse<Response> saveOrderResponse = saveOrderRequest(member, orderRequest);
        long orderId = getIdFromCreatedResponse(saveOrderResponse);

        ExtractableResponse<Response> findOrderDetailResponse = findOrderDetailRequest(member, orderId);
        OrderDetailResponse orderDetailResponse = findOrderDetailResponse.jsonPath()
                .getObject(".", OrderDetailResponse.class);
        // then
        assertAll(
                () -> assertThat(saveOrderResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(orderDetailResponse.getUsedPoint()).isEqualTo(300),
                () -> assertThat(orderDetailResponse.getSavedPoint()).isEqualTo(1000),
                () -> assertThat(orderDetailResponse.getProducts()).hasSize(2)
        );
    }

    private ExtractableResponse<Response> saveOrderRequest(Member member, OrderRequest orderRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> findOrderDetailRequest(Member member, Long orderId) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/" + orderId)
                .then()
                .log().all()
                .extract();
    }

    private long getIdFromCreatedResponse(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }
}
