package cart.integration;

import static io.restassured.RestAssured.given;

import cart.application.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.application.dto.cartitem.CartRequest;
import cart.application.dto.coupon.CouponRequest;
import cart.application.dto.member.MemberJoinRequest;
import cart.application.dto.member.MemberLoginRequest;
import cart.application.dto.order.OrderRequest;
import cart.application.dto.product.ProductRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Sql("classpath:/init.sql")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    final String LOCATION = "Location";

    void 사용자_저장(final MemberJoinRequest 사용자_저장_요청) {
        given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(사용자_저장_요청)
            .when()
            .post("/users/join")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    void 상품_저장(ProductRequest 상품_저장_요청) {
        given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(상품_저장_요청)
            .when()
            .post("/products")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    void 장바구니_상품_저장(final MemberLoginRequest 사용자_로그인_요청, final CartRequest 장바구니_저장_요청) {
        given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(사용자_로그인_요청.getName(), 사용자_로그인_요청.getPassword())
            .body(장바구니_저장_요청)
            .when()
            .post("/cart-items")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    void 장바구니_상품_수량_수정(final MemberLoginRequest 사용자_로그인_요청,
                       final CartItemQuantityUpdateRequest 장바구니_수량_수정_요청,
                       final Long cartItemId) {
        given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(사용자_로그인_요청.getName(), 사용자_로그인_요청.getPassword())
            .when()
            .body(장바구니_수량_수정_요청)
            .patch("/cart-items/{cartItemId}", cartItemId)
            .then()
            .statusCode(HttpStatus.OK.value());
    }

    void 쿠폰_저장(final CouponRequest 쿠폰_등록_요청) {
        given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(쿠폰_등록_요청)
            .when()
            .post("/coupons")
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    void 주문_저장(final MemberLoginRequest 사용자_로그인_요청, final OrderRequest 주문_등록_요청) {
        given()
            .auth().preemptive().basic(사용자_로그인_요청.getName(), 사용자_로그인_요청.getPassword())
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(주문_등록_요청)
            .post("/orders")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .header(LOCATION, "/orders/" + 1);
    }
}
