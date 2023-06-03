package cart.ui.product;

import cart.WebMvcConfig;
import cart.application.repository.product.ProductRepository;
import cart.domain.product.Product;
import cart.ui.product.dto.ProductRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static cart.fixture.ProductFixture.배변패드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:/reset.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SuppressWarnings("NonAsciiCharacters")
class ProductWriteControllerTest {

    @Autowired
    private ProductRepository productRepository;

    @MockBean
    WebMvcConfig webMvcConfig;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("POST /products 상품을 추가한다.")
    void createProductTest() {
        final ProductRequest productRequest = new ProductRequest("휴지", 1000, "휴지이미지");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(productRequest)
                .when().post("/products")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank(),
                () -> assertThat(productRepository.findAll().get(0)).usingRecursiveComparison().ignoringFields("id").isEqualTo(productRequest)
        );
    }

    @Test
    @DisplayName("PUT /products 상품을 수정한다.")
    void updateProductTest() {
        final Long productId = productRepository.createProduct(배변패드);
        final ProductRequest productRequest = new ProductRequest("휴지", 1000, "휴지이미지");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(productRequest)
                .when().put("/products/" + productId)
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(productRepository.findAll().get(0)).usingRecursiveComparison().isEqualTo(
                        new Product(productId, productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl())
                )
        );
    }

    @Test
    @DisplayName("PUT /products 존재하지 않는 상품 수정 BAD REQUEST")
    void updateProduct_DoesNotExistTest() {
        final Long productId = productRepository.createProduct(배변패드);
        final ProductRequest productRequest = new ProductRequest("휴지", 1000, "휴지이미지");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(productRequest)
                .when().put("/products/" + productId + 1)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("DELETE /products 상품을 제거한다.")
    void deleteProductTest() {
        final Long productId = productRepository.createProduct(배변패드);
        final ProductRequest productRequest = new ProductRequest("휴지", 1000, "휴지이미지");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/products/" + productId)
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(productRepository.findAll()).hasSize(0)
        );
    }

}
