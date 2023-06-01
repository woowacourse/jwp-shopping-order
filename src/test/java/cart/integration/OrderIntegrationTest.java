package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.OrderCartItemDto;
import cart.dto.OrderCartItemsRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.fixtures.ProductFixtures.CHICKEN;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberDao.getMemberById(1L);
    }

    @Test
    void 장바구니에_있는_상품들을_주문한다() {
        final OrderCartItemDto orderCartItemDto = new OrderCartItemDto(1L, CHICKEN.NAME, CHICKEN.PRICE, CHICKEN.IMAGE_URL);
        final OrderCartItemsRequest request = new OrderCartItemsRequest(List.of(orderCartItemDto));

        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(request)
                .when()
                .post("/orders")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 주문_목록을_확인하다() {
        final ExtractableResponse<Response> response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 주문_상세를_확인하다() {
        final ExtractableResponse<Response> response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/{orderId}", 1L)
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
