package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.CartItemRequest;
import cart.dto.OrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Long cartItemId;
    private Long cartItemId2;
    private Member member;
    private Member member2;

    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberDao.getMemberById(1L).get();
        member2 = memberDao.getMemberById(2L).get();

        cartItemId = createCartItem(member, new CartItemRequest(5L));
        cartItemId2 = createCartItem(member, new CartItemRequest(6L));
    }

    private Long createCartItem(final Member member, final CartItemRequest cartItemRequest) {
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract();

        return getIdFromCreatedResponse(response);
    }

    private long getIdFromCreatedResponse(final ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("location").split("/")[2]);
    }

    @DisplayName("주문 정보를 추가한다.")
    @Test
    void createOrder() {
        final ExtractableResponse<Response> response = 주문_정보_추가(member,
                new OrderRequest(List.of(cartItemId, cartItemId2)));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/orders/1");
    }

    private ExtractableResponse<Response> 주문_정보_추가(final Member member, final OrderRequest orderRequest) {
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

    @DisplayName("잘못된 사용자 정보로 주문 정보 추가 요청시 실패한다.")
    @Test
    void addCartItemByIllegalMember() {
        final Member illegalMember = new Member(member.getId(), member.getEmail(), member.getPassword() + "asdf");
        final OrderRequest orderRequest = new OrderRequest(List.of(cartItemId, cartItemId2));
        final ExtractableResponse<Response> response = 주문_정보_추가(illegalMember, orderRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("장바구니 정보와 다른 사용자 정보로 주문 정보 추가 요청시 실패한다.")
    @Test
    void addCartItemByDifferentMember() {
        final OrderRequest orderRequest = new OrderRequest(List.of(cartItemId, cartItemId2));
        final ExtractableResponse<Response> response = 주문_정보_추가(member2, orderRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
