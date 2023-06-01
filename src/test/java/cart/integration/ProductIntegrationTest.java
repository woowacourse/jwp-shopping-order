package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dto.PagedProductsResponse;
import cart.dto.PaginationInfoDto;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

public class ProductIntegrationTest extends IntegrationTest {

    @Test
    public void getProducts() {
        var result = allProductsSelectRequest();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void createProduct() {
        var product = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");

        var response = productCreateRequest(product);

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

    @Sql("/productBatchInsert.sql")
    @DisplayName("특정 페이지의 상품만 조회한다")
    @Test
    void getPagedProducts() {
        // given
        int unitSize = 5;
        int page = 2;
        var allProducts = allProductsSelectRequest().as(List.class);
        int expectedTotalPage = allProducts.size() / unitSize + 1;

        // when
        final ExtractableResponse<Response> response = given().log().all()
                .pathParam("unit-size", unitSize)
                .pathParam("page", page)
                .when().get("/products")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        PagedProductsResponse pagedProductsResponse = response.as(PagedProductsResponse.class);
        final PaginationInfoDto pagination = pagedProductsResponse.getPagination();

        // then
        assertThat(pagedProductsResponse.getProducts()).hasSize(unitSize);
        assertThat(pagination.getPerPage()).isEqualTo(unitSize);
        assertThat(pagination.getCurrentPage()).isEqualTo(page);
        assertThat(pagination.getLastPage()).isEqualTo(expectedTotalPage);
        assertThat(pagination.getTotal()).isEqualTo(allProducts.size());
    }

    private ExtractableResponse<Response> allProductsSelectRequest() {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> productCreateRequest(final ProductRequest product) {
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when()
                .post("/products")
                .then()
                .extract();
    }
}
