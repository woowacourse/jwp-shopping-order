package cart.integration;

import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ProductIntegrationTest extends IntegrationTest {

    @BeforeEach
    void setUp() {
        super.setUp();
        databaseSetting.clearDatabase();
        databaseSetting.createTables();
    }

    @Test
    public void getProducts() {
        var result = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products")
                .then().log().all()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void 특정_상품의_정보를_가져올_수_있다() {
        super.databaseSetting.addProducts();

        var result = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products?ids=1,3")
                .then().log().all()
                .extract();

        final List<ProductResponse> productResponses = result.jsonPath().getList(".", ProductResponse.class);

        assertAll(
                () -> assertThat(productResponses).hasSize(2),
                () -> assertThat(productResponses).allMatch(productResponse -> productResponse.getId() != 2),
                () -> assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }

    @Test
    public void query스트링이_형식이_맞지않으면_400를_발생시킨다() {
        var result = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products?ids=1^^3")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(result.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(result.jsonPath().getInt("errorCode")).isNotNull()
        );
    }

    @Test
    public void id가_존재하지_않으면_404을_응답한다() {
        databaseSetting.addProducts();

        var result = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products?ids=1000,1")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(result.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(result.jsonPath().getInt("errorCode")).isNotNull()
        );
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
}
