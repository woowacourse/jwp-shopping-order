package cart.ui.api;

import cart.application.request.OrderRequest;
import cart.config.ControllerTestConfig;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import cart.fixture.dao.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static cart.fixture.domain.CartItemsFixture.장바구니_상품_목록;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

class OrderApiControllerTest extends ControllerTestConfig {

    Member 회원;
    Product 상품;
    CartItem 장바구니_상품;
    Long 주문_식별자값;
    LocalDateTime 주문_시간;

    @BeforeEach
    void setUp() {
        주문_시간 = now();
        회원 = new MemberDaoFixture(memberDao).회원을_등록한다("a@a.com", "1234", "100000", "10000");
        상품 = new ProductDaoFixture(productDao).상품을_등록한다("계란", 1000);
        장바구니_상품 = new CartItemDaoFixture(cartItemDao).장바구니_상품을_등록한다(상품, 회원, 10);
        주문_식별자값 = new OrderDaoFixture(orderDao).주문을_등록한다(회원, 장바구니_상품_목록(List.of(장바구니_상품)), "1000", 주문_시간);
        new OrderItemDaoFixture(orderItemDao).주문_장바구니_상품을_등록한다(주문_식별자값, 장바구니_상품);
    }

    @Test
    void 회원이_선택한_장바구니_상품_목록을_주문한다() {
        given(spec)
                .log().all()
                .filter(document(DOCUMENT_IDENTIFIER,
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("basic 64인코딩값")
                        ),
                        requestFields(
                                fieldWithPath("cartItemIds").description("장바구니 상품 식별자값 목록"),
                                fieldWithPath("point").description("사용할 포인트")
                        ),
                        responseHeaders(
                                headerWithName(LOCATION).description("주문 식별자값")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .log().all()
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword())
                .body(new OrderRequest(List.of(장바구니_상품.getId()), 1000))
                .post("/orders")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 주문_식별자값으로_회원의_주문을_조회한다() {
        given(spec)
                .log().all()
                .filter(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("id").description("주문 식별자값")
                        ),
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("basic 64인코딩값")
                        ),
                        responseFields(
                                fieldWithPath("orderId").description("주문 식별자값"),
                                fieldWithPath("orderProducts.[].productId").description("주문한 상품 식별자값"),
                                fieldWithPath("orderProducts.[].name").description("주문한 상품명"),
                                fieldWithPath("orderProducts.[].quantity").description("주문한 상품 수량"),
                                fieldWithPath("orderProducts.[].price").description("주문한 단일 상품 금액"),
                                fieldWithPath("orderProducts.[].totalPrice").description("주문한 해당 상품의 총금액 (수량 * 금액)"),
                                fieldWithPath("orderTotalPrice").description("주문 총금액"),
                                fieldWithPath("usedPoint").description("사용한 포인트"),
                                fieldWithPath("createdAt").description("주문 시간")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .log().all()
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword())
                .pathParam("id", 주문_식별자값)
                .get("/orders/{id}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 회원의_모든_주문을_조회한다() {
        given(spec)
                .log().all()
                .filter(document(DOCUMENT_IDENTIFIER,
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("basic 64인코딩값")
                        ),
                        responseFields(
                                fieldWithPath("[].orderId").description("주문 식별자값"),
                                fieldWithPath("[].orderProducts.[].productId").description("주문한 상품 식별자값"),
                                fieldWithPath("[].orderProducts.[].name").description("주문한 상품명"),
                                fieldWithPath("[].orderProducts.[].quantity").description("주문한 상품 수량"),
                                fieldWithPath("[].orderProducts.[].price").description("주문한 단일 상품 금액"),
                                fieldWithPath("[].orderProducts.[].totalPrice").description("주문한 해당 상품의 총금액 (수량 * 금액)")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .log().all()
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword())
                .get("/orders")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
