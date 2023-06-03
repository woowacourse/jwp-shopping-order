package cart.integration;

import cart.domain.product.Product;
import cart.dto.PageInfo;
import cart.dto.PagingProductResponse;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class ProductIntegrationTest extends IntegrationTest {

    @Sql("classpath:testData.sql")
    @Test
    public void getProducts() {
        //when
        var result = given().log().all()
                .param("page", 1)
                .param("size", 10)
                .when()
                .get("/products")
                .then()
                .extract();

        //then
        final PagingProductResponse response = result.as(PagingProductResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(response.getPageInfo()).usingRecursiveComparison()
                    .isEqualTo(new PageInfo(1, 10, 21, 3));
            softly.assertThat(response.getProducts()).usingRecursiveComparison()
                    .isEqualTo(List.of(
                            ProductResponse.of(new Product(1L, "1", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(2L, "2", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(3L, "3", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(4L, "4", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(5L, "5", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(6L, "6", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(7L, "7", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(8L, "8", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(9L, "9", 1000, "image.jpeg")),
                            ProductResponse.of(new Product(10L, "10", 1000, "image.jpeg"))
                    ));
        });
    }

    @Test
    public void createProduct() {
        var product = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");

        var response = given().log().all()
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
                given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(product)
                        .when()
                        .post("/products")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().header("Location");

        // get product
        var responseProduct = given().log().all()
                .when()
                .get(location)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", ProductResponse.class);

        assertThat(responseProduct.getId()).isNotNull();
        assertThat(responseProduct.getName()).isEqualTo("피자");
        assertThat(responseProduct.getPrice()).isEqualTo(15_000);
    }
}
