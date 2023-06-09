package cart.integration;

import cart.domain.Member;
import cart.domain.Point;
import cart.dto.*;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Sql({"classpath:/testSchema.sql", "classpath:/testData.sql"})
public class OrderIntegrationTest extends IntegrationTest {

    @Test
    void 주문을_생성하면_결제금액에_비례하여_포인트가_쌓인다() {
        final Member member = new Member(1L, "tmdgh1592@naver.com", "1234", new Point(BigDecimal.ZERO));
        final OrderRequest orderRequest = new OrderRequest(List.of(new OrderItem(1L)), new PaymentDto(20000L, 20000L, 0L));
        final ExtractableResponse<Response> createResponse = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders")
                .then()
                .log().all()
                .extract();
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        final PointResponse pointResponse = given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/points")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", PointResponse.class);

        assertThat(pointResponse.getAvailablePoint()).isEqualTo(2000L);
    }

    @Test
    void 사용_포인트_만큼_포인트_잔고에서_차감된다() {
        final Member member = new Member(1L, "tmdgh1592@naver.com", "1234", new Point(BigDecimal.ZERO));
        final OrderRequest orderRequest = new OrderRequest(List.of(new OrderItem(1L)), new PaymentDto(20000L, 20000L, 0L));
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders");

        final OrderRequest newOrderRequest = new OrderRequest(List.of(new OrderItem(2L)), new PaymentDto(80000L, 78000L, 2000L));
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(newOrderRequest)
                .when()
                .post("/orders");

        final PointResponse pointResponse = given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/points")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", PointResponse.class);
        assertThat(pointResponse.getAvailablePoint()).isEqualTo(7800L);
    }

    @Test
    void 주문이_완료되면_장바구니_물품이_삭제된다() {
        final Member member = new Member(1L, "tmdgh1592@naver.com", "1234", new Point(BigDecimal.ZERO));
        final OrderRequest orderRequest = new OrderRequest(List.of(new OrderItem(1L)), new PaymentDto(20000L, 20000L, 0L));

        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when()
                .post("/orders");

        final List object = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/cart-items")
                .then()
                .log().all()
                .extract()
                .jsonPath()
                .getObject(".", List.class);

        assertThat(object.size()).isEqualTo(1);
    }

}
