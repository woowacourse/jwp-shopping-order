package cart.controller.product;

import cart.dto.product.ProductRequest;
import cart.dto.sale.SaleProductRequest;
import cart.service.product.ProductService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
public class ProductControllerIntegrationTest {

    @Autowired
    private ProductService productService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @DisplayName("모든 상품을 조회한다.")
    @Test
    void find_all_products() {
        // given
        productService.createProduct(new ProductRequest("치킨", 10000, "img"));

        // when & then
        Response response = given()
                .when().get("/products");

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", equalTo(1))
                .body("[0].name", equalTo("치킨"))
                .body("[0].price", equalTo(10000))
                .body("[0].imageUrl", equalTo("img"))
                .body("[0].isOnSale", equalTo(false))
                .body("[0].salePrice", equalTo(0));
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void find_product_by_id() {
        // given
        productService.createProduct(new ProductRequest("치킨", 10000, "img"));

        // when & then
        Response response = given()
                .when().get("/products/1");

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(1))
                .body("name", equalTo("치킨"))
                .body("price", equalTo(10000))
                .body("imageUrl", equalTo("img"))
                .body("isOnSale", equalTo(false))
                .body("salePrice", equalTo(0));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create_product() {
        // given
        ProductRequest req = new ProductRequest("치킨", 10000, "img");

        // when & then
        Response response = given()
                .contentType(ContentType.JSON)
                .body(req)
                .when().post("/products");

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .header("location", "/products/1");
    }

    @DisplayName("상품을 수정한다.")
    @Test
    void update_product() {
        // given
        ProductRequest req = new ProductRequest("치킨", 10000, "img");
        ProductRequest editReq = new ProductRequest("치킨2", 20000, "img2");

        long id = productService.createProduct(req);

        // when & then
        Response response = given()
                .contentType(ContentType.JSON)
                .body(editReq)
                .when().put("/products/" + id);

        response.then()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("상품을 삭제한다.")
    @Test
    void delete_product() {
        // given
        ProductRequest req = new ProductRequest("치킨", 10000, "img");
        long id = productService.createProduct(req);

        // when & then
        Response response = given()
                .when().delete("/products/" + id);

        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("상품에 세일을 적용한다.")
    @Test
    void apply_sale_on_product() {
        // given
        ProductRequest req = new ProductRequest("치킨", 10000, "img");
        long id = productService.createProduct(req);
        SaleProductRequest saleReq = new SaleProductRequest(10);

        // when & then
        Response response = given()
                .contentType(ContentType.JSON)
                .body(saleReq)
                .when().post("/products/" + id + "/sales");

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .header("location", "/products/" + id);
    }

    @DisplayName("상품에 세일을 취소한다.")
    @Test
    void un_apply_sale_on_product() {
        // given
        ProductRequest req = new ProductRequest("치킨", 10000, "img");
        long id = productService.createProduct(req);
        SaleProductRequest saleReq = new SaleProductRequest(10);
        productService.applySale(id, saleReq);

        // when & then
        Response response = given()
                .contentType(ContentType.JSON)
                .body(saleReq)
                .when().delete("/products/" + id + "/sales");

        response.then()
                .statusCode(HttpStatus.OK.value())
                .header("location", "/products/" + id);
    }
}
