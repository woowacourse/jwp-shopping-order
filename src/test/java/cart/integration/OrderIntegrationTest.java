package cart.integration;

import cart.controller.dto.request.OrderRequest;
import cart.controller.dto.request.ProductRequest;
import cart.integration.common.AuthInfo;
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
    private long cartItemId1;
    private long cartItemId2;

    @BeforeEach
    void setUp() {
        final long productId1 = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
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
            final ExtractableResponse<Response> response = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().preemptive().basic(member.getEmail(), member.getPassword())
                    .body(request)
                    .when().post("/orders")
                    .then()
                    .extract();

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
            final ExtractableResponse<Response> response = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().preemptive().basic(member.getEmail(), member.getPassword())
                    .body(request)
                    .when().post("/orders")
                    .then()
                    .extract();

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
            final ExtractableResponse<Response> response = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().preemptive().basic(member.getEmail(), member.getPassword())
                    .body(request)
                    .when().post("/orders")
                    .then()
                    .extract();

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
            final ExtractableResponse<Response> response = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .auth().preemptive().basic(member.getEmail(), member.getPassword())
                    .body(request)
                    .when().post("/orders")
                    .then()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }
    }
}
