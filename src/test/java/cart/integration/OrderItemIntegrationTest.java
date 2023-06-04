package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.CartItem;
import cart.domain.Member;
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

        // then
        assertAll(
                () -> assertThat(saveOrderResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value())
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
}
