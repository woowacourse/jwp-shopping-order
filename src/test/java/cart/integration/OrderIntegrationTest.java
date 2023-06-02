package cart.integration;

import cart.controller.dto.request.OrderRequest;
import cart.controller.dto.request.ProductRequest;
import cart.integration.common.AuthInfo;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 통합 테스트")
@Sql({"/truncate.sql", "/member_data.sql"})
public class OrderIntegrationTest extends IntegrationTest {

    private final AuthInfo member = new AuthInfo("a@a.com", "1234");
    private final AuthInfo otherMember = new AuthInfo("b@b.com", "1234");
    private final ProductRequest mainProductRequest = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");
    private long cartItemId1;
    private long cartItemId2;

    @BeforeEach
    void setUp() {
        super.setUp();

        final long productId1 = createProduct(mainProductRequest);
        final long productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));
        cartItemId1 = requestAddCartItemAndGetId(member, productId1);
        cartItemId2 = requestAddCartItemAndGetId(member, productId2);
    }

    @Nested
    @DisplayName("주문 요청")
    class Order {

        @Test
        @DisplayName("성공 - 단일 상품 주문")
        void success_single_product_order() {
            // given
            final int paymentAmount = 10_000;
            final OrderRequest request = new OrderRequest(List.of(cartItemId1), paymentAmount);

            // when
            final ExtractableResponse<Response> response = requestAddOrder(member, request);

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                    () -> assertThat(response.header("Location")).isNotBlank()
            );
        }

        @Test
        @DisplayName("성공 - 다중 상품 주문")
        void success_multi_product_order() {
            // given
            final int paymentAmount = 25_000;
            final OrderRequest request = new OrderRequest(List.of(cartItemId1, cartItemId2), paymentAmount);

            // when
            final ExtractableResponse<Response> response = requestAddOrder(member, request);

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                    () -> assertThat(response.header("Location")).isNotBlank()
            );
        }

        @Test
        @DisplayName("실패 - 금액 불일치")
        void fail_wrong_payment_amount() {
            // given
            final int wrongPaymentAmount = 23_000;
            final OrderRequest request = new OrderRequest(List.of(cartItemId1, cartItemId2), wrongPaymentAmount);

            // when
            final ExtractableResponse<Response> response = requestAddOrder(member, request);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("실패 - 유저 인증 실패")
        void fail_unauthorized() {
            // given
            final int paymentAmount = 25_000;
            final OrderRequest request = new OrderRequest(List.of(cartItemId1, cartItemId2), paymentAmount);

            // when
            final ExtractableResponse<Response> response = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().preemptive().basic(member.getEmail(), "invalid password")
                    .body(request)
                    .when().post("/orders")
                    .then()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 장바구니 상품")
        void fail_cartItem_not_found() {
            // given
            final Long wrongProductId = 77777L;
            final int paymentAmount = 25_000;
            final OrderRequest request = new OrderRequest(List.of(cartItemId1, wrongProductId), paymentAmount);

            // when
            final ExtractableResponse<Response> response = requestAddOrder(member, request);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }
    }

    @Nested
    @DisplayName("주문 목록 조회")
    class GetAll {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            final int paymentAmount = 25_000;
            final OrderRequest request = new OrderRequest(List.of(cartItemId1, cartItemId2), paymentAmount);
            final long id = getIdFromCreatedResponse(requestAddOrder(member, request));

            // when
            final ExtractableResponse<Response> response = given()
                    .auth().preemptive().basic(member.getEmail(), member.getPassword())
                    .when().get("/orders")
                    .then()
                    .extract();

            // then
            final Configuration conf = Configuration.defaultConfiguration();
            final DocumentContext documentContext = JsonPath.using(conf).parse(response.asString());

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(documentContext.read("$.size()", Integer.class)).isEqualTo(1),
                    () -> assertThat(documentContext.read("$[0].id", Long.class)).isEqualTo(id),
                    () -> assertThat(documentContext.read("$[0].mainProductName", String.class))
                            .isEqualTo(mainProductRequest.getName()),
                    () -> assertThat(documentContext.read("$[0].mainProductImage", String.class))
                            .isEqualTo(mainProductRequest.getImageUrl()),
                    () -> assertThat(documentContext.read("$[0].extraProductCount", Integer.class)).isEqualTo(1),
                    () -> assertThat(documentContext.read("$[0].price", Integer.class)).isEqualTo(paymentAmount)
            );
        }

        @Test
        @DisplayName("성공 - 주문이 없는 경우")
        void success_when_no_order() {
            // given, when
            final ExtractableResponse<Response> response = given()
                    .auth().preemptive().basic(member.getEmail(), member.getPassword())
                    .when().get("/orders")
                    .then()
                    .extract();

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(response.body().asString()).isEqualTo("[]")
            );

        }
    }

    @Nested
    @DisplayName("주문 상세 조회")
    class Get {

        @Test
        @DisplayName("성공 - 3만원 미만 주문")
        void success_when_under_30000() {
            // given
            final int paymentAmount = 25_000;
            final OrderRequest request = new OrderRequest(List.of(cartItemId1, cartItemId2), paymentAmount);
            final long id = getIdFromCreatedResponse(requestAddOrder(member, request));

            // when
            final ExtractableResponse<Response> response = given()
                    .auth().preemptive().basic(member.getEmail(), member.getPassword())
                    .when().get("/orders/{id}", id)
                    .then()
                    .extract();

            // then
            final Configuration conf = Configuration.defaultConfiguration();
            final DocumentContext documentContext = JsonPath.using(conf).parse(response.asString());

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(documentContext.read("$.priceBeforeDiscount", Integer.class)).isEqualTo(25_000),
                    () -> assertThat(documentContext.read("$.priceAfterDiscount", Integer.class)).isEqualTo(25_000),
                    () -> assertThat(documentContext.read("$.orderItems.size()", Integer.class))
                            .isEqualTo(request.getCartItems().size()),
                    () -> assertThat(documentContext.read("$.orderItems[0].name", String.class)).isEqualTo("치킨"),
                    () -> assertThat(documentContext.read("$.orderItems[1].name", String.class)).isEqualTo("피자")
            );
        }

        @Test
        @DisplayName("성공 - 3만원 이상 5만원 미만 주문")
        void success_when_between_30000_and_50000() {
            // given
            final int paymentAmount = 33_000;
            final long productId3 = createProduct(mainProductRequest);
            final long cartItemId3 = requestAddCartItemAndGetId(member, productId3);
            final OrderRequest request = new OrderRequest(List.of(cartItemId1, cartItemId2, cartItemId3), paymentAmount);
            final long id = getIdFromCreatedResponse(requestAddOrder(member, request));

            // when
            final ExtractableResponse<Response> response = given()
                    .auth().preemptive().basic(member.getEmail(), member.getPassword())
                    .when().get("/orders/{id}", id)
                    .then()
                    .extract();

            // then
            final Configuration conf = Configuration.defaultConfiguration();
            final DocumentContext documentContext = JsonPath.using(conf).parse(response.asString());

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(documentContext.read("$.priceBeforeDiscount", Integer.class)).isEqualTo(35_000),
                    () -> assertThat(documentContext.read("$.priceAfterDiscount", Integer.class)).isEqualTo(33_000),
                    () -> assertThat(documentContext.read("$.orderItems.size()", Integer.class))
                            .isEqualTo(request.getCartItems().size()),
                    () -> assertThat(documentContext.read("$.orderItems[0].name", String.class)).isEqualTo("치킨"),
                    () -> assertThat(documentContext.read("$.orderItems[1].name", String.class)).isEqualTo("피자"),
                    () -> assertThat(documentContext.read("$.orderItems[2].name", String.class)).isEqualTo("치킨")
            );
        }

        @Test
        @DisplayName("성공 - 5만원 이상 주문")
        void success_when_over_50000() {
            // given
            final int paymentAmount = 55_000;
            final long productId = createProduct(new ProductRequest("치킨", 60_000, "http://example.com/chicken.jpg"));
            final long cartItemId = requestAddCartItemAndGetId(member, productId);
            final OrderRequest request = new OrderRequest(List.of(cartItemId), paymentAmount);
            final long id = getIdFromCreatedResponse(requestAddOrder(member, request));

            // when
            final ExtractableResponse<Response> response = given()
                    .auth().preemptive().basic(member.getEmail(), member.getPassword())
                    .when().get("/orders/{id}", id)
                    .then()
                    .extract();

            // then
            final Configuration conf = Configuration.defaultConfiguration();
            final DocumentContext documentContext = JsonPath.using(conf).parse(response.asString());

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(documentContext.read("$.priceBeforeDiscount", Integer.class)).isEqualTo(60_000),
                    () -> assertThat(documentContext.read("$.priceAfterDiscount", Integer.class)).isEqualTo(55_000),
                    () -> assertThat(documentContext.read("$.orderItems.size()", Integer.class))
                            .isEqualTo(request.getCartItems().size()),
                    () -> assertThat(documentContext.read("$.orderItems[0].name", String.class)).isEqualTo("치킨")
            );
        }

        @Test
        @DisplayName("실패 - 다른 유저의 주문")
        void fail_when_not_members_order() {
            // given
            final int paymentAmount = 25_000;
            final OrderRequest request = new OrderRequest(List.of(cartItemId1, cartItemId2), paymentAmount);
            final long id = getIdFromCreatedResponse(requestAddOrder(member, request));

            // when
            final ExtractableResponse<Response> response = given()
                    .auth().preemptive().basic(otherMember.getEmail(), otherMember.getPassword())
                    .when().get("/orders/{id}", id)
                    .then()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 order id")
        void fail_when_order_not_found() {
            // given
            final long invalidId = 9999L;

            // when
            final ExtractableResponse<Response> response = given()
                    .auth().preemptive().basic(member.getEmail(), member.getPassword())
                    .when().get("/orders/{id}", invalidId)
                    .then()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }
    }

    private ExtractableResponse<Response> requestAddOrder(AuthInfo member, OrderRequest orderRequest) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(orderRequest)
                .when().post("/orders")
                .then()
                .extract();
    }
}
