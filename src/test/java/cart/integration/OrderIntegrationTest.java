package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.OrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;
    private Member member;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberDao.getMemberById(1L);
    }

    @Test
    @DisplayName("장바구니에 담긴 상품을 주문한다.")
    void order() {
        //given
        final ExtractableResponse<Response> response = createOrder(List.of(1L, 2L));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("존재하지 않는 cartItemId가 포함되어 있는 경우 400을 반환한다.")
    void orderFailByNonExistedCartItem() {
        //given
        final ExtractableResponse<Response> response = createOrder(List.of(1L, 2L, 10L));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("다른 멤버의 장바구니 아이템을 구매하려는 경우 403을 반환한다.")
    void orderFailByMemberDoesNotHaveCartItem() {
        //given
        final ExtractableResponse<Response> response = createOrder(List.of(1L, 2L, 3L));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("존재하지 않는 cartItemId를 통해 구매를 하는 경우 400을 응답한다.")
    void orderFailByUnExistedCartItem() {
        //given
        final ExtractableResponse<Response> response = createOrder(List.of(1L, 2L, 10L));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("orderId를 통해 주문내역을 조회한다.")
    void findOrderById() {
        //given
        final ExtractableResponse<Response> order = createOrder(List.of(1L, 2L));
        final long orderId = Long.parseLong(order.header("Location").split("/")[2]);

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/" + orderId)
                .then()
                .log().all()
                .extract();

        //then
        assertThat(response.jsonPath().getLong("id")).isEqualTo(orderId);
    }

    @Test
    @DisplayName("회원의 모든 결제내역을 반환한다.")
    void findOrdersByMember() {
        //given
        createOrder(List.of(1L));
        createOrder(List.of(2L));

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> createOrder(List<Long> cartItemIds) {
        final OrderRequest orderRequest = new OrderRequest(cartItemIds);

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
