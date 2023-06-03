package cart.ui.order;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

import cart.application.repository.MemberRepository;
import cart.application.repository.order.OrderRepository;
import cart.application.service.member.MemberReadService;
import cart.application.service.member.dto.MemberResultDto;
import cart.application.service.order.OrderReadService;
import cart.application.service.order.dto.OrderItemDto;
import cart.ui.order.dto.OrderResponse;
import cart.ui.order.dto.OrdersResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "/reset.sql",executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:/test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class OrderReadControllerTest {

    @Autowired
    private MemberReadService memberReadService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("사용자의 주문 목록을 조회한다.")
    void findOrders() {
        MemberResultDto memberById = memberReadService.findMemberById(5L);

        String base64Credentials = java.util.Base64.getEncoder().encodeToString((memberById.getEmail() + ":" + memberById.getPassword()).getBytes());
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Basic " + base64Credentials)
                .when().get("/orders")
                .then().log().all()
                .extract();

        assertSoftly(softly -> {
            OrdersResponse ordersResponse = response.as(OrdersResponse.class);
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(ordersResponse.getOrderResponses().size()).isOne();
            softly.assertThat(ordersResponse.getOrderResponses().get(0).getUsedPoint()).isZero();
            softly.assertThat(ordersResponse.getOrderResponses().get(0).getPaymentPrice()).isEqualTo(3000);
        });
    }

    @Test
    @DisplayName("사용자의 특정 주문 상세정보를 조회한다.")
    void findProductsByOrder() {
        final MemberResultDto memberById = memberReadService.findMemberById(5L);
        final int testOrderId = 100;
        String base64Credentials = java.util.Base64.getEncoder().encodeToString((memberById.getEmail() + ":" + memberById.getPassword()).getBytes());
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("Authorization", "Basic " + base64Credentials)
                .when().get("/orders/" + testOrderId)
                .then().log().all()
                .extract();

        List<OrderItemDto> expectOrderItem = List.of(new OrderItemDto(100L, "레오배변패드", 30000, 1, "image"));

        assertSoftly(softly -> {
            OrderResponse orderResponse = response.as(OrderResponse.class);
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(orderResponse.getPaymentPrice()).isEqualTo(3000);
            softly.assertThat(orderResponse.getOrderItems()).usingRecursiveComparison().isEqualTo(expectOrderItem);
        });
    }

}
