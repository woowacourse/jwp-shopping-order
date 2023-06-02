package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.dto.OrderCreateRequest;
import cart.dto.OrderItemSelectResponse;
import cart.dto.OrderSelectResponse;
import cart.dto.OrderSimpleInfoResponse;
import cart.repository.dao.MemberDao;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
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

    private Member member;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberDao.getMemberById(1L);
    }

    @DisplayName("주문을 생성한다")
    @Test
    void createOrder() {
        // given
        final List<Long> cartItemIds = List.of(1L, 2L);
        final int totalPrice = 95_000;
        final OrderCreateRequest request = new OrderCreateRequest(cartItemIds, totalPrice);

        // when
        final ExtractableResponse<Response> response = orderCreateRequest(request);
        final Long saveId = Long.parseLong(response.header("Location").split("/")[2]);

        // then
        assertThat(saveId).isNotNull();
    }

    @DisplayName("주문을 단건 조회한다")
    @Test
    void showOrderById() {
        // given
        final List<Long> cartItemIds = List.of(1L, 2L);
        final int totalPrice = 95_000;
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(cartItemIds, totalPrice);
        final ExtractableResponse<Response> createResponse = orderCreateRequest(orderCreateRequest);
        final Long saveId = Long.parseLong(createResponse.header("Location").split("/")[2]);

        // when
        final ExtractableResponse<Response> response = orderSelectRequest(saveId);
        final OrderSelectResponse orderSelectResponse = response.as(OrderSelectResponse.class);

        // then
        assertThat(orderSelectResponse.getId()).isEqualTo(saveId);
        assertThat(orderSelectResponse.getOriginalPrice()).isEqualTo(100_000);
        assertThat(orderSelectResponse.getDiscountPrice()).isEqualTo(5_000);
        assertThat(orderSelectResponse.getFinalPrice()).isEqualTo(totalPrice);
        assertThat(orderSelectResponse.getOrderProducts()).map(OrderItemSelectResponse::getId)
                .isEqualTo(cartItemIds);
    }

    @DisplayName("주문을 전체 조회한다")
    @Test
    void showOrders() {
        // given
        final List<Long> cartItemIds1 = List.of(1L);
        final int totalPrice1 = 20_000;
        final OrderCreateRequest orderCreateRequest1 = new OrderCreateRequest(cartItemIds1, totalPrice1);
        final ExtractableResponse<Response> createResponse1 = orderCreateRequest(orderCreateRequest1);
        final Long saveId1 = Long.parseLong(createResponse1.header("Location").split("/")[2]);

        final List<Long> cartItemIds2 = List.of(2L);
        final int totalPrice2 = 78_000;
        final OrderCreateRequest orderCreateRequest2 = new OrderCreateRequest(cartItemIds2, totalPrice2);
        final ExtractableResponse<Response> createResponse2 = orderCreateRequest(orderCreateRequest2);
        final Long saveId2 = Long.parseLong(createResponse2.header("Location").split("/")[2]);

        // when
        final ExtractableResponse<Response> response = selectAllOrdersByMemberRequest();
        final List<OrderSimpleInfoResponse> ordersSelectResponse = Arrays.asList(response.as(OrderSimpleInfoResponse[].class));

        // then
        assertThat(ordersSelectResponse).map(OrderSimpleInfoResponse::getId)
                .containsExactly(saveId1, saveId2);
    }

    private ExtractableResponse<Response> orderCreateRequest(final OrderCreateRequest orderCreateRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderCreateRequest)
                .when().post("/orders")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private ExtractableResponse<Response> orderSelectRequest(final Long saveId) {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/orders/" + saveId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private ExtractableResponse<Response> selectAllOrdersByMemberRequest() {
        return RestAssured.given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/orders")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

}
