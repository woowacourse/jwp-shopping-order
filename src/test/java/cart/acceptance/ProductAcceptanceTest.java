package cart.acceptance;

import static cart.fixtures.CartItemFixtures.Dooly_CartItem1;
import static cart.fixtures.CartItemFixtures.Dooly_CartItem2;
import static cart.fixtures.MemberFixtures.Dooly;
import static cart.fixtures.ProductFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import cart.domain.product.Product;
import cart.dto.ProductRequest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("상품 추가 시")
    class createProduct {

        @Nested
        @DisplayName("상품 정보를 검증한다.")
        class validateProduct {

            @ParameterizedTest
            @ValueSource(strings = {" ", ""})
            @DisplayName("상품 이름이 빈 값이면 예외가 발생한다.")
            void throws_when_name_blank(String productName) {
                // given
                ProductRequest productRequest = new ProductRequest(productName, CHICKEN.PRICE, CHICKEN.IMAGE_URL);

                // when
                String responseBody = RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(productRequest)
                        .when()
                        .post("/products")
                        .then().log().all()
                        .extract().body().asString();

                // then
                assertThat(responseBody).isEqualTo("상품 이름을 입력해주세요.");
            }

            @ParameterizedTest
            @ValueSource(strings = {"@!@E", "1234", "@3121$"})
            @DisplayName("상품 이름이 한글과 영어가 아니면 예외가 발생한다.")
            void throws_when_wrong_product_name(String productName) {
                // given
                ProductRequest productRequest = new ProductRequest(productName, CHICKEN.PRICE, CHICKEN.IMAGE_URL);

                // when
                String responseBody = RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(productRequest)
                        .when()
                        .post("/products")
                        .then().log().all()
                        .extract().body().asString();

                // then
                assertThat(responseBody).isEqualTo("상품 이름은 한글 또는 영어여야합니다.");
            }

            @ParameterizedTest
            @ValueSource(ints = {0, -1})
            @DisplayName("상품 가격이 0이하이면 예외가 발생한다.")
            void throws_when_price_not_positive(int productPrice) {
                // given
                ProductRequest productRequest = new ProductRequest(CHICKEN.NAME, productPrice, CHICKEN.IMAGE_URL);

                // when
                String responseBody = RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(productRequest)
                        .when()
                        .post("/products")
                        .then().log().all()
                        .extract().body().asString();

                // then
                assertThat(responseBody).isEqualTo("가격은 양수여야합니다.");
            }

            @ParameterizedTest
            @ValueSource(strings = {" ", ""})
            @DisplayName("상품 이미지 URL이 빈 값이면 예외가 발생한다.")
            void throws_when_image_url_blank(String productImageUrl) {
                // given
                ProductRequest productRequest = new ProductRequest(CHICKEN.NAME, CHICKEN.PRICE, productImageUrl);

                // when
                String responseBody = RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(productRequest)
                        .when()
                        .post("/products")
                        .then().log().all()
                        .extract().body().asString();

                // then
                assertThat(responseBody).isEqualTo("상품 이미지 URL을 입력해주세요.");
            }

            @ParameterizedTest
            @ValueSource(strings = {"aaa://", "shop.com"})
            @DisplayName("상품 이미지 URL이 올바른 형식이 아니면 예외가 발생한다.")
            void throws_when_image_url_wrong_pattern(String productImageUrl) {
                // given
                ProductRequest productRequest = new ProductRequest(CHICKEN.NAME, CHICKEN.PRICE, productImageUrl);

                // when
                String responseBody = RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(productRequest)
                        .when()
                        .post("/products")
                        .then().log().all()
                        .extract().body().asString();

                // then
                assertThat(responseBody).isEqualTo("올바른 URL 형식으로 입력해주세요.");
            }
        }

        @Test
        @DisplayName("정상적으로 상품이 추가된다.")
        void success() {
            // given
            ProductRequest productRequest = new ProductRequest(CHICKEN.NAME, CHICKEN.PRICE, CHICKEN.IMAGE_URL);

            // when
            Response response = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(productRequest)
                    .when()
                    .post("/products")
                    .then().log().all()
                    .extract().response();

            // then
            assertThat(response.getHeader("Location")).contains("/products/");
        }
    }

    @Nested
    @DisplayName("상품 목록 조회 시")
    class getAllProductCartItems {

        @Test
        @DisplayName("인증 정보가 없으면 예외가 발생한다.")
        void throws_when_not_found_authentication() {
            // when
            Response response = RestAssured.given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .get("/products/cart-items")
                    .then().log().all()
                    .extract().response();

            assertThat(response.getBody().asString()).isEqualTo("인증 정보가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("인증된 사용자가 아니면 예외가 발생한다.")
        void throws_when_not_authentication_user() {
            // given
            String email = "notExist@email.com";
            String password = "notExistPassword";

            // whe
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(email, password)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .get("/products/cart-items")
                    .then().log().all()
                    .extract().response();

            assertThat(response.getBody().asString()).isEqualTo("인증된 사용자가 아닙니다.");
        }

        @Test
        @DisplayName("첫 페이지일 때 상품 정보, 장바구니 상품 정보, 마지막 상품 여부를 반환한다.")
        void getAllPagingProductCartItems_first() {
            // given
            Map<String, Object> params = Map.of(
                    "lastId", 0,
                    "pageItemCount", 3
            );
            Product product1 = PANCAKE.ENTITY;
            Product product2 = PIZZA.ENTITY;
            Product product3 = SALAD.ENTITY;

            // when
            Response response = RestAssured.given().log().all()
                    .params(params)
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .get("/products/cart-items")
                    .then().log().all()
                    .extract().response();

            JsonPath jsonPath = response.jsonPath();
            // then
            assertAll(
                    () -> assertThat(jsonPath.getLong("products[0].product.id")).isEqualTo(product1.getId()),
                    () -> assertThat(jsonPath.getString("products[0].product.name")).isEqualTo(product1.getName()),
                    () -> assertThat(jsonPath.getString("products[0].product.imageUrl")).isEqualTo(product1.getImageUrl()),
                    () -> assertThat(jsonPath.getInt("products[0].product.price")).isEqualTo(product1.getPrice()),
                    () -> assertThat((String) jsonPath.get("cartItem")).isNull(),

                    () -> assertThat(jsonPath.getLong("products[1].product.id")).isEqualTo(product2.getId()),
                    () -> assertThat(jsonPath.getString("products[1].product.name")).isEqualTo(product2.getName()),
                    () -> assertThat(jsonPath.getString("products[1].product.imageUrl")).isEqualTo(product2.getImageUrl()),
                    () -> assertThat(jsonPath.getInt("products[1].product.price")).isEqualTo(product2.getPrice()),
                    () -> assertThat((String) jsonPath.get("cartItem")).isNull(),

                    () -> assertThat(jsonPath.getLong("products[2].product.id")).isEqualTo(product3.getId()),
                    () -> assertThat(jsonPath.getString("products[2].product.name")).isEqualTo(product3.getName()),
                    () -> assertThat(jsonPath.getString("products[2].product.imageUrl")).isEqualTo(product3.getImageUrl()),
                    () -> assertThat(jsonPath.getInt("products[2].product.price")).isEqualTo(product3.getPrice()),
                    () -> assertThat(jsonPath.getLong("products[2].cartItem.id")).isEqualTo(Dooly_CartItem2.ID),
                    () -> assertThat(jsonPath.getLong("products[2].cartItem.quantity")).isEqualTo(Dooly_CartItem2.QUANTITY));

        }

        @Test
        @DisplayName("첫 페이지가 아닐 때 상품 정보, 장바구니 상품 정보, 마지막 상품 여부를 반환한다.")
        void getAllPagingProductCartItems_not_first() {
            // given
            Map<String, Object> params = Map.of(
                    "lastId", 2,
                    "pageItemCount", 3
            );
            Product product1 = CHICKEN.ENTITY;

            // when
            Response response = RestAssured.given().log().all()
                    .params(params)
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .get("/products/cart-items")
                    .then().log().all()
                    .extract().response();

            JsonPath jsonPath = response.jsonPath();

            // then
            assertAll(
                    () -> assertThat(jsonPath.getLong("products[0].product.id")).isEqualTo(product1.getId()),
                    () -> assertThat(jsonPath.getString("products[0].product.name")).isEqualTo(product1.getName()),
                    () -> assertThat(jsonPath.getString("products[0].product.imageUrl")).isEqualTo(product1.getImageUrl()),
                    () -> assertThat(jsonPath.getInt("products[0].product.price")).isEqualTo(product1.getPrice()),
                    () -> assertThat((String) jsonPath.get("cartItem")).isNull());
        }
    }


    @Nested
    @DisplayName("상품 상세 조회 시")
    class getProductCartItems {

        @Test
        @DisplayName("인증 정보가 없으면 예외가 발생한다.")
        void throws_when_not_found_authentication() {
            // when
            Product product = Dooly_CartItem1.PRODUCT;
            Response response = RestAssured.given().log().all()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .get("/products/" + product.getId() + "/cart-items")
                    .then().log().all()
                    .extract().response();

            assertThat(response.getBody().asString()).isEqualTo("인증 정보가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("인증된 사용자가 아니면 예외가 발생한다.")
        void throws_when_not_authentication_user() {
            // given
            String email = "notExist@email.com";
            String password = "notExistPassword";

            // when
            Product product = Dooly_CartItem1.PRODUCT;
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(email, password)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .get("/products/" + product.getId() + "/cart-items")
                    .then().log().all()
                    .extract().response();

            assertThat(response.getBody().asString()).isEqualTo("인증된 사용자가 아닙니다.");
        }

        @Test
        @DisplayName("조회할 상품 ID가 존재하지 않으면 예외가 발생한다.")
        void throws_when_not_fount_product() {
            // given
            Long notExistId = -1L;

            // when, then
            Response response = RestAssured.given().log().all()
                    .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .get("/products/" + notExistId + "/cart-items")
                    .then().log().all()
                    .extract().response();

            assertThat(response.getBody().asString()).isEqualTo("상품 ID에 해당하는 상품을 찾을 수 없습니다.");
        }

        @Nested
        @DisplayName("조회할 상품 ID에 해당하는 상품이 존재할 떄")
        class existProduct {

            @Test
            @DisplayName("상품이 해당 유저의 장바구니에 담겨 있으면 상품 정보와 장바구니에 담긴 상품 정보가 함께 조회된다.")
            void getCartProduct_contains() {
                // when
                Product product = Dooly_CartItem1.PRODUCT;
                Response response = RestAssured.given().log().all()
                        .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .get("/products/" + product.getId() + "/cart-items")
                        .then().log().all()
                        .extract().response();

                JsonPath jsonPath = response.jsonPath();

                // then
                assertAll(
                        () -> assertThat(jsonPath.getLong("product.id")).isEqualTo(product.getId()),
                        () -> assertThat(jsonPath.getString("product.name")).isEqualTo(product.getName()),
                        () -> assertThat(jsonPath.getString("product.imageUrl")).isEqualTo(product.getImageUrl()),
                        () -> assertThat(jsonPath.getInt("product.price")).isEqualTo(product.getPrice()),
                        () -> assertThat(jsonPath.getLong("cartItem.id")).isEqualTo(Dooly_CartItem1.ID),
                        () -> assertThat(jsonPath.getInt("cartItem.quantity")).isEqualTo(Dooly_CartItem1.QUANTITY)
                );
            }

            @Test
            @DisplayName("상품이 해당 유저의 장바구니에 담겨 있지 않으면 상품 정보만 조회된다.")
            void getCartProduct_not_contains() {
                // when
                Product product = PANCAKE.ENTITY;
                Response response = RestAssured.given().log().all()
                        .auth().preemptive().basic(Dooly.EMAIL, Dooly.PASSWORD)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .get("/products/" + product.getId() + "/cart-items")
                        .then().log().all()
                        .extract().response();

                JsonPath jsonPath = response.jsonPath();

                // then
                assertAll(
                        () -> assertThat(jsonPath.getLong("product.id")).isEqualTo(product.getId()),
                        () -> assertThat(jsonPath.getString("product.name")).isEqualTo(product.getName()),
                        () -> assertThat(jsonPath.getString("product.imageUrl")).isEqualTo(product.getImageUrl()),
                        () -> assertThat(jsonPath.getInt("product.price")).isEqualTo(product.getPrice()),
                        () -> assertThat((String) jsonPath.get("cartItem")).isNull()
                );
            }
        }
    }
}
