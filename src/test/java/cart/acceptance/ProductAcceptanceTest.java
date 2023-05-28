package cart.acceptance;

import static cart.fixtures.CartItemFixtures.Dooly_CartItem1;
import static cart.fixtures.MemberFixtures.Dooly;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Product;
import cart.fixtures.ProductFixtures;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

            System.out.println(response.getStatusCode());
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
                Product product = ProductFixtures.PANCAKE.ENTITY;
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
