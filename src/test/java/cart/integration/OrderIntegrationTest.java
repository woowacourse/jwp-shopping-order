package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.request.OrderRequest;
import cart.entity.MemberEntity;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Member member;


    @BeforeEach
    void setUp() {
        super.setUp();

        final MemberEntity memberEntity = memberDao.findById(1L);

        member = Member.from(memberEntity);
    }

    @Test
    void 장바구니에_담겨있는_상품을_주문할_수_있다() {
        // given
        final OrderRequest orderRequest = new OrderRequest(List.of(1L, 2L));

        // when
        final var result = 상품_주문(member, orderRequest);

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 상품_주문(final Member member, final OrderRequest orderRequest) {
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

    @Test
    void id로_주문_상세정보를_확인할_수_있다() {
        // given
        final ExtractableResponse<Response> response = 상품_주문(member, new OrderRequest(List.of(1L)));
        final long orderId = extractIdFromResponseHeader(response);

        // when
        final var result = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/{orderId}", orderId)
                .then()
                .log().all()
                .body("orderId", equalTo(1))
                .body("totalPrice", equalTo(20000))
                .body("cartItemResponse[0].id", equalTo(null))
                .body("cartItemResponse[0].quantity", equalTo(2))
                .extract();

        // expect
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 사용자의_모든_주문_정보를_확인할_수_있다() {
        // given
        상품_주문(member, new OrderRequest(List.of(1L)));
        상품_주문(member, new OrderRequest(List.of(2L)));

        // when
        final var result = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .body("[0].orderId", equalTo(1))
                .body("[0].totalPrice", equalTo(20000))
                .body("[0].cartItemResponse[0].id", equalTo(null))
                .body("[0].cartItemResponse[0].quantity", equalTo(2))
                .body("[1].orderId", equalTo(2))
                .body("[1].totalPrice", equalTo(80000))
                .body("[1].cartItemResponse[0].id", equalTo(null))
                .body("[1].cartItemResponse[0].quantity", equalTo(4))
                .extract();

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static long extractIdFromResponseHeader(final ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }
}
