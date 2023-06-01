package cart.acceptance;

import static cart.fixtures.CartItemFixtures.*;
import static cart.fixtures.MemberFixtures.Dooly;
import static cart.fixtures.OrderFixtures.Dooly_Order1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import cart.domain.cartitem.CartItem;
import cart.domain.product.Product;
import cart.dto.MemberCashChargeRequest;
import cart.dto.OrderCartItemDto;
import cart.dto.OrderRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class OrderAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("장바구니에 담긴 상품을 주문할 때")
    class orderCartItem {

        @Test
        @DisplayName("인증 정보가 없으면 예외가 발생한다.")
        void throws_when_not_found_authentication() {
            // given
            OrderRequest request = Dooly_Order1.REQUEST();

            // when
            Response response = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/orders")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증 정보가 존재하지 않습니다.")
            );
        }

        @Test
        @DisplayName("Authorization 헤더의 값이 Basic으로 시작하지 않으면 예외가 발생한다.")
        void throws_when_authorization_not_start_basic() {
            // given
            OrderRequest request = Dooly_Order1.REQUEST();

            // when
            Response response = RestAssured.given().log().all()
                    .header(HttpHeaders.AUTHORIZATION, "NO BASIC")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/orders")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("BASIC 인증 정보가 존재하지 않습니다. PREFIX로 BASIC을 넣어주세요.")
            );
        }

        @Test
        @DisplayName("인증된 사용자가 아니면 예외가 발생한다.")
        void throws_when_not_authentication_user() {
            // given
            String email = "notExist@email.com";
            String password = "notExistPassword";
            OrderRequest request = Dooly_Order1.REQUEST();

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(email, password)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/orders")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증된 사용자가 아닙니다.")
            );
        }

        @Nested
        @DisplayName("인증된 사용자와 사용자의 장바구니 아이디 리스트를 담아서 요청할 시")
        class requestWithAuthMemberAndCartItemIds {

            @Test
            @DisplayName("요청 받은 장바구니 상품 아이디 중 하나라도 해당 사용자의 장바구니 상품이 아니면 예외가 발생한다.")
            void throws_when_request_cartItemIds_not_member_cartItem() {
                // given
                CartItem cartItem1 = Dooly_CartItem1.ENTITY();
                Product product1 = Dooly_CartItem1.PRODUCT;
                CartItem cartItem2 = Ber_CartItem1.ENTITY();
                Product product2 = Ber_CartItem1.PRODUCT;

                List<OrderCartItemDto> orderCartItemDtos = List.of(
                        new OrderCartItemDto(cartItem1.getId(), product1.getName(), product1.getPrice(), product1.getImageUrl()),
                        new OrderCartItemDto(cartItem2.getId(), product2.getName(), product2.getPrice(), product2.getImageUrl())
                );
                OrderRequest request = new OrderRequest(orderCartItemDtos);

                // when
                Response response = RestAssured.given().log().all()
                        .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                        .post("/orders")
                        .then().log().all()
                        .extract().response();

                // then
                assertAll(
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                        () -> assertThat(response.getBody().asString()).isEqualTo("장바구니 상품에 없는 상품입니다.")
                );
            }

            @Test
            @DisplayName("총 주문 금액보다 사용자의 현재 잔액이 적다면 예외가 발생한다.")
            void throws_when_current_member_cash_less() {
                // given
                OrderRequest request =  Dooly_Order1.REQUEST();

                // when
                Response response = RestAssured.given().log().all()
                        .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                        .post("/orders")
                        .then().log().all()
                        .extract().response();

                // then
                assertAll(
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                        () -> assertThat(response.getBody().asString()).isEqualTo("사용자의 현재 금액이 주문 금액보다 적습니다.")
                );
            }

            @Nested
            @DisplayName("주문 시 상품 정보가 변경될 때")
            class updateProductInfo {

                @Test
                @DisplayName("상품 이름이 변경되면 예외가 발생한다.")
                void throws_when_update_productName() {
                    // given
                    OrderRequest orderRequest = Dooly_Order1.UPDATE_NAME_REQUEST();
                    String emptyBody = "";

                    // when
                    Response response = RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(orderRequest)
                            .post("/orders")
                            .then().log().all()
                            .extract().response();

                    // then
                    assertAll(
                            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                            () -> assertThat(response.getBody().asString()).isEqualTo("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.")
                    );
                }

                @Test
                @DisplayName("상품 가격이 변경되면 예외가 발생한다.")
                void throws_when_update_productPrice() {
                    // given
                    OrderRequest orderRequest = Dooly_Order1.UPDATE_PRICE_REQUEST();

                    // when
                    Response response = RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(orderRequest)
                            .post("/orders")
                            .then().log().all()
                            .extract().response();

                    // then
                    assertAll(
                            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                            () -> assertThat(response.getBody().asString()).isEqualTo("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.")
                    );
                }

                @Test
                @DisplayName("상품 이미지 URL이 변경되면 예외가 발생한다.")
                void throws_when_update_productImageUrl() {
                    // given
                    OrderRequest orderRequest = Dooly_Order1.UPDATE_IMAGE_URL_REQUEST();

                    // when
                    Response response = RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(orderRequest)
                            .post("/orders")
                            .then().log().all()
                            .extract().response();

                    // then
                    assertAll(
                            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                            () -> assertThat(response.getBody().asString()).isEqualTo("주문 상품 정보와 기존 상품 정보가 일치하지 않습니다.")
                    );
                }
            }

            @Nested
            @DisplayName("정상적으로 주문이 될 때")
            class successOrder {

                @Test
                @DisplayName("주문이 성공한다.")
                void success() {
                    // given
                    int cashToCharge = 150000;
                    MemberCashChargeRequest chargeRequest = new MemberCashChargeRequest(cashToCharge);
                    OrderRequest orderRequest = Dooly_Order1.REQUEST();
                    String emptyBody = "";

                    // when
                    RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(chargeRequest)
                            .post("/members/cash")
                            .then().log().all();

                    Response response = RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(orderRequest)
                            .post("/orders")
                            .then().log().all()
                            .extract().response();

                    // then
                    assertAll(
                            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.value()),
                            () -> assertThat(response.getHeader("Location")).contains("/orders/"),
                            () -> assertThat(response.getBody().asString()).isEqualTo(emptyBody)
                    );
                }

                @Test
                @DisplayName("사용자의 현재 금액에서 총 주문 금액을 차감한다.")
                void payCash() {
                    // given
                    int cashToCharge = 150000;
                    MemberCashChargeRequest chargeRequest = new MemberCashChargeRequest(cashToCharge);
                    OrderRequest orderRequest =  Dooly_Order1.REQUEST();
                    int chargedCash = Dooly.CASH + cashToCharge;
                    int totalPrice = Dooly_CartItem1.PRICE + Dooly_CartItem2.PRICE;
                    int cashAfterOrder = chargedCash - totalPrice;

                    // when
                    RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(chargeRequest)
                            .post("/members/cash")
                            .then().log().all();

                    RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(orderRequest)
                            .post("/orders")
                            .then().log().all();

                    Response response = RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .get("/members/cash")
                            .then().log().all()
                            .extract().response();

                    // then
                    assertAll(
                            () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value()),
                            () -> assertThat(response.jsonPath().getInt("totalCash")).isEqualTo(cashAfterOrder)
                    );
                }

                @Test
                @DisplayName("주문이 되면 사용자의 장바구니 상품에서 주문한 장바구니 상품을 삭제한다.")
                void deleteMemberCartItem() {
                    // given
                    int cashToCharge = 150000;
                    MemberCashChargeRequest chargeRequest = new MemberCashChargeRequest(cashToCharge);
                    OrderRequest orderRequest =  Dooly_Order1.REQUEST();

                    RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(chargeRequest)
                            .post("/members/cash")
                            .then().log().all();

                    RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(orderRequest)
                            .post("/orders")
                            .then().log().all();

                    Response response = RestAssured.given().log().all()
                            .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                            .get("/cart-items")
                            .then().log().all()
                            .extract().response();

                    assertThat(response.getBody().asString()).isEqualTo("[]");
                }
            }
        }
    }

}
