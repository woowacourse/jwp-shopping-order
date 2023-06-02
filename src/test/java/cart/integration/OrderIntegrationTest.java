package cart.integration;

import cart.domain.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static cart.fixtures.MemberFixtures.MEMBER1;
import static cart.fixtures.OrderFixtures.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("주문 목록을 가져온다.")
    void showOrdersTest() throws JsonProcessingException {
        ExtractableResponse<Response> response = requestGetOrders(MEMBER1);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).isEqualTo(objectMapper.writeValueAsString(List.of(ORDER_RESPONSE1, ORDER_RESPONSE2)));
    }

    private ExtractableResponse<Response> requestGetOrders(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();
    }

    @Test
    @DisplayName("주문 상세 정보를 가져온다.")
    void showOrderDetailsTest() throws JsonProcessingException {
        ExtractableResponse<Response> response = requestGetOrderDetails(MEMBER1);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).isEqualTo(objectMapper.writeValueAsString(ORDER_DETAIL_RESPONSE));
    }

    private ExtractableResponse<Response> requestGetOrderDetails(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/1")
                .then()
                .log().all()
                .extract();
    }
}
