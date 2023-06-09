package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductIntegrationTest extends IntegrationTest {

    @Test
    public void getProducts() {
        var result = given(this.spec)
                .filter(
                        document("get-products",
                                responseFields(
                                        fieldWithPath("[].id").description("상품 아이디"),
                                        fieldWithPath("[].name").description("상품명"),
                                        fieldWithPath("[].price").description("상품 가격"),
                                        fieldWithPath("[].imageUrl").description("상품 이미지 주소")
                                ))
                )
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

        var response = given(this.spec)
                .filter(
                        document("create-product",
                                requestFields(
                                        fieldWithPath("name").description("상품명"),
                                        fieldWithPath("price").description("상품 가격"),
                                        fieldWithPath("imageUrl").description("상품 이미지 주소")
                                )
                        )
                )
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
                given(this.spec)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(product)
                        .when()
                        .post("/products")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().header("Location");
        Long productId = Long.valueOf(location.split("/")[2]);

        // get product
        var responseProduct = given(this.spec).log().all()
                .filter(
                        document("get-product",
                                pathParameters(
                                        parameterWithName("productId").description("확인할 상품 id")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("상품 id"),
                                        fieldWithPath("name").description("상품명"),
                                        fieldWithPath("price").description("상품 가격"),
                                        fieldWithPath("imageUrl").description("상품 이미지 주소")
                                )
                        )
                )
                .when()
                .get("/products/{productId}", productId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getObject(".", ProductResponse.class);

        assertThat(responseProduct.getId()).isNotNull();
        assertThat(responseProduct.getName()).isEqualTo("피자");
        assertThat(responseProduct.getPrice()).isEqualTo(15_000);
    }

    @Test
    void deleteProduct() {
        var product = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");

        // create product
        var location =
                given(this.spec)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(product)
                        .when()
                        .post("/products")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().header("Location");
        Long productId = Long.valueOf(location.split("/")[2]);
        var response = given(this.spec).log().all()
                .filter(document("delete-product",
                                pathParameters(
                                        parameterWithName("productId").description("확인할 상품 id")
                                )
                        )
                )
                .when()
                .delete("/products/{productId}", productId)
                .then()
                .extract();
        assertThat(response.statusCode()).isEqualTo(204);
    }

    @Test
    void updateProduct(){
        var product = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");

        // create product
        var location =
                given(this.spec)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(product)
                        .when()
                        .post("/products")
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract().header("Location");
        Long productId = Long.valueOf(location.split("/")[2]);
        var newProduct = new ProductRequest("새로운 피자", product.getPrice(), product.getImageUrl());

        // update product
        var response = given(this.spec).log().all()
                .filter(document("update-product",
                                pathParameters(
                                        parameterWithName("productId").description("수정할 상품 id")
                                )
                        )
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(newProduct)
                .when()
                .put("/products/{productId}", productId)
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(200);
    }
}
