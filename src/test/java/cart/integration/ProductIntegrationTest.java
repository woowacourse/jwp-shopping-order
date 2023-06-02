package cart.integration;

import cart.controller.dto.request.ProductRequest;
import cart.controller.dto.response.ProductResponse;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("상품 통합 테스트")
public class ProductIntegrationTest extends IntegrationTest {

    @Test
    public void getProducts() {
        var result = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products")
                .then()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void createProduct() {
        var product = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");

        var response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when()
                .post("/products")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    public void getCreatedProduct() {
        var product = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");

        // create product
        var location =
                given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(product)
                        .when()
                        .post("/products")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().header("Location");

        // get product
        var responseProduct = given().log().all()
                .when()
                .get(location)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", ProductResponse.class);

        assertThat(responseProduct.getId()).isNotNull();
        assertThat(responseProduct.getName()).isEqualTo("피자");
        assertThat(responseProduct.getPrice()).isEqualTo(15_000);
    }

    @Nested
    @DisplayName("원하는 상품 목록 조회")
    class GetMultiple {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            final String ids = "1,2,3";

            // when
            final ExtractableResponse<Response> response = given()
                    .pathParam("ids", ids)
                    .when().get("/products")
                    .then()
                    .extract();

            // then
            final Configuration conf = Configuration.defaultConfiguration();
            final DocumentContext documentContext = JsonPath.using(conf).parse(response.asString());

            assertAll(
                    () -> assertThat(documentContext.read("$.size()", Integer.class)).isEqualTo(3),
                    () -> assertThat(documentContext.read("$[0].id", Long.class)).isEqualTo(1L),
                    () -> assertThat(documentContext.read("$[1].id", Long.class)).isEqualTo(2L),
                    () -> assertThat(documentContext.read("$[2].id", Long.class)).isEqualTo(3L)
            );
        }

        @Test
        @DisplayName("실패 - 잘못된 path parameter 형식")
        void fail_when_invalid_path_parameter_format() {
            // given
            final String ids = "1-2-3";

            // when
            final ExtractableResponse<Response> response = given()
                    .pathParam("ids", ids)
                    .when().get("/products")
                    .then()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 product id")
        void fail_when_product_not_found() {
            // given
            final String ids = "1,2,3,1111";

            // when
            final ExtractableResponse<Response> response = given()
                    .pathParam("ids", ids)
                    .when().get("/products")
                    .then()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }
    }
}
