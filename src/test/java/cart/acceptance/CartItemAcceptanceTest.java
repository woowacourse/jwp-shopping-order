package cart.acceptance;

import static cart.fixtures.CartItemFixtures.*;
import static cart.fixtures.MemberFixtures.*;
import static cart.fixtures.ProductFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CartItemAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("추가할 상품 ID와 인증된 사용자를 담아서 장바구니 상품 담기 요청을 보낼 시")
    class addCartItems {

        @Test
        @DisplayName("사용자의 장바구니에 이미 상품이 존재하면 합친 수량으로 업데이트된다.")
        void updateCartItem_when_cartItem_isExist() {
            // given
            Long existProductId = Bixx_CartItem1.PRODUCT.getId();
            int requestQuantity = 5;
            String emptyBody = "";
            CartItemRequest request = new CartItemRequest(existProductId, requestQuantity);

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(Bixx.EMAIL, Bixx.PASSWORD)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/cart-items")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.value()),
                    () -> assertThat(response.getHeader("Location")).contains("/cart-items/"),
                    () -> assertThat(response.getBody().asString()).isEqualTo(emptyBody)
            );
        }

        @Test
        @DisplayName("인증 정보가 없으면 예외가 발생한다.")
        void throws_when_not_found_authentication() {
            // given
            CartItemRequest request = new CartItemRequest(CHICKEN.ID, 2);

            // when
            Response response = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/cart-items")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증 정보가 존재하지 않습니다.")
            );
        }

        @Test
        @DisplayName("인증된 사용자가 아니면 예외가 발생한다.")
        void throws_when_not_authentication_user() {
            // given
            String email = "notExist@email.com";
            String password = "notExistPassword";
            CartItemRequest request = new CartItemRequest(CHICKEN.ID, 2);

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(email, password)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/cart-items")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증된 사용자가 아닙니다.")
            );
        }

        @Test
        @DisplayName("상품 ID에 해당하는 상품이 존재하지 않으면 예외가 발생한다.")
        void throws_when_product_not_exist() {
            // given
            Long notExistProductId = -1L;
            CartItemRequest request = new CartItemRequest(notExistProductId, 2);

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/cart-items")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("상품 ID에 해당하는 상품을 찾을 수 없습니다.")
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 0})
        @DisplayName("추가할 수량이 1 이상의 정수가 아니면 예외가 발생한다.")
        void throws_when_product_Quantity_not_positive(int quantity) {
            // given
            Long notExistProductId = -1L;
            CartItemRequest request = new CartItemRequest(notExistProductId, quantity);

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/cart-items")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("수량은 양수여야합니다.")
            );
        }

        @Test
        @DisplayName("정상적으로 장바구니에 상품이 담긴다.")
        void success() {
            // given
            Long newProductId = PANCAKE.ID;
            int requestQuantity = 5;
            CartItemRequest request = new CartItemRequest(newProductId, requestQuantity);
            String emptyBody = "";

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .post("/cart-items")
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED.value()),
                    () -> assertThat(response.getHeader("Location")).contains("/cart-items/"),
                    () -> assertThat(response.getBody().asString()).isEqualTo(emptyBody)
            );
        }
    }

    @Nested
    @DisplayName("수정할 상품 수량과 인증된 사용자, 상품 ID를 담아서 장바구니 상품 수량 수정 요청을 보낼 시")
    class updateCartItemQuantity {

        @Test
        @DisplayName("인증 정보가 없으면 예외가 발생한다.")
        void throws_when_not_found_authentication() {
            // given
            Long cartItemId = Dooly_CartItem1.ID;
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(2);

            // when
            Response response = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .patch("/cart-items/{id}", cartItemId)
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증 정보가 존재하지 않습니다.")
            );
        }

        @Test
        @DisplayName("인증된 사용자가 아니면 예외가 발생한다.")
        void throws_when_not_authentication_user() {
            // given
            String email = "notExist@email.com";
            String password = "notExistPassword";
            Long cartItemId = Dooly_CartItem1.ID;
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(2);

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(email, password)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .patch("/cart-items/{id}", cartItemId)
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증된 사용자가 아닙니다.")
            );
        }

        @Test
        @DisplayName("장바구니 상품 ID에 해당하는 장바구니 상품이 존재하지 않으면 예외가 발생한다.")
        void throws_when_cartItem_not_exist() {
            // given
            Long notExistCartItemId = -1L;
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(2);

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .patch("/cart-items/{id}", notExistCartItemId)
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("장바구니 상품 ID에 해당하는 장바구니 상품을 찾을 수 없습니다.")
            );
        }

        @Test
        @DisplayName("수정할 수량이 0 이상의 정수가 아니면 예외가 발생한다.")
        void throws_when_cartItem_Quantity_not_positive() {
            // given
            int wrongQuantity = -1;
            Long cartItemId = Dooly_CartItem1.ID;
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(wrongQuantity);

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .patch("/cart-items/{id}", cartItemId)
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("수정할 수량은 0 이상인 정수여야합니다.")
            );
        }

        @Test
        @DisplayName("수정할 수량이 0이면 장바구니 상품을 삭제한다.")
        void delete_cartItem_when_cartItem_Quantity_0() {
            // given
            int quantity = 0;
            Long cartItemId = Dooly_CartItem2.ID;
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(quantity);

            // when
            RestAssured.given().log().all()
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .patch("/cart-items/{id}", cartItemId)
                    .then().log().all();

            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .get("/cart-items")
                    .then().log().all()
                    .extract().response();
            JsonPath jsonPath = response.jsonPath();

            // then
            assertThat(jsonPath.prettyPrint()).doesNotContain(SALAD.NAME);
        }

        @Test
        @DisplayName("사용자가 가진 장바구니 상품이 아니면 예외가 발생한다.")
        void throws_when_cartItem_wrong_owner() {
            // given
            int quantity = 5;
            Long cartItemId = Bixx_CartItem1.ID;
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(quantity);

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .patch("/cart-items/{id}", cartItemId)
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("해당 사용자의 장바구니에 담긴 상품이 아닙니다.")
            );
        }

        @Test
        @DisplayName("정상적으로 장바구니 상품의 수량이 수정된다.")
        void success() {
            // given
            int quantity = Bixx_CartItem1.QUANTITY + 1;
            Long cartItemId = Bixx_CartItem1.ID;
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(quantity);

            // when
            RestAssured.given().log().all()
                    .auth().preemptive().basic(Bixx.EMAIL, Bixx.PASSWORD)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .patch("/cart-items/{id}", cartItemId)
                    .then().log().all()
                    .extract().response();

            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(Bixx.EMAIL, Bixx.PASSWORD)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .get("products/{id}/cart-items", SALAD.ID)
                    .then().log().all()
                    .extract().response();
            JsonPath jsonPath = response.jsonPath();

            // then
            assertThat(jsonPath.getInt("cartItem.quantity")).isEqualTo(quantity);
        }
    }

    @Nested
    @DisplayName("장바구니에 담긴 상품 삭제 시")
    class deleteCartItem {

        @Test
        @DisplayName("인증 정보가 없으면 예외가 발생한다.")
        void throws_when_not_found_authentication() {
            // given
            Long cartItemId = Dooly_CartItem1.ID;
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(2);

            // when
            Response response = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .delete("/cart-items/{id}", cartItemId)
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증 정보가 존재하지 않습니다.")
            );
        }

        @Test
        @DisplayName("인증된 사용자가 아니면 예외가 발생한다.")
        void throws_when_not_authentication_user() {
            // given
            String email = "notExist@email.com";
            String password = "notExistPassword";
            Long cartItemId = Dooly_CartItem1.ID;
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(2);

            // when
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(email, password)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .delete("/cart-items/{id}", cartItemId)
                    .then().log().all()
                    .extract().response();

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                    () -> assertThat(response.getBody().asString()).isEqualTo("인증된 사용자가 아닙니다.")
            );
        }

        @Nested
        @DisplayName("인증된 사용자와 삭제할 장바구니 상품 ID를 담아서 요청한다.")
        class requestWithAuthMemberAndCartItemId {

            @Test
            @DisplayName("삭제할 장바구니 상품 ID가 존재하지 않으면 예외가 발생한다.")
            void throws_when_cartItem_not_exist() {
                // given
                Long notExistCartItemId = -1L;

                // when
                Response response = RestAssured.given().log().all()
                        .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .delete("/cart-items/{id}", notExistCartItemId)
                        .then().log().all()
                        .extract().response();

                // then
                assertAll(
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                        () -> assertThat(response.getBody().asString()).isEqualTo("장바구니 상품 ID에 해당하는 장바구니 상품을 찾을 수 없습니다.")
                );
            }

            @Test
            @DisplayName("사용자가 가진 장바구니 상품이 아니면 예외가 발생한다.")
            void throws_when_cartItem_wrong_owner() {
                // given
                Long cartItemId = Bixx_CartItem1.ID;

                // when
                Response response = RestAssured.given().log().all()
                        .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .delete("/cart-items/{id}", cartItemId)
                        .then().log().all()
                        .extract().response();

                // then
                assertAll(
                        () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
                        () -> assertThat(response.getBody().asString()).isEqualTo("해당 사용자의 장바구니에 담긴 상품이 아닙니다.")
                );
            }

            @Test
            @DisplayName("정상적으로 장바구니 상품이 삭제된다.")
            void success() {
                // given
                Long cartItemId = Ber_CartItem2.ID;

                // when
                RestAssured.given().log().all()
                        .auth().preemptive().basic(Ber.EMAIL, Ber.PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .delete("/cart-items/{id}", cartItemId)
                        .then().log().all();

                Response response = RestAssured.given().log().all()
                        .auth().preemptive().basic(Ber.EMAIL, Ber.PASSWORD)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .get("products/{id}/cart-items", PIZZA.ID)
                        .then().log().all()
                        .extract().response();
                JsonPath jsonPath = response.jsonPath();

                // then
                assertThat(jsonPath.getObject("cartItem", Object.class)).isNull();
            }
        }
    }
}
