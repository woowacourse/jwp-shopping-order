package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import io.restassured.common.mapper.TypeRef;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;


@Sql(scripts = {"/delete.sql", "/schema.sql"})
public class ProductIntegrationTest extends IntegrationTest {

    @Autowired
    private ProductDao productDao;
    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        super.setUp();

        Long productId1 = productDao.createProduct(new Product("치킨", 10000,
                "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80",
                30));
        Long productId2 = productDao.createProduct(new Product("샐러드", 20000,
                "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80",
                50));
        Long productId3 = productDao.createProduct(new Product("피자", 13000,
                "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80",
                70));

        product1 = productDao.getProductById(productId1);
        product2 = productDao.getProductById(productId2);
        product3 = productDao.getProductById(productId3);
    }

    @Test
    public void 모든_상품을_가져온다() {
        var response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<ProductResponse> productResponses = response.as(new TypeRef<>() {});
        List<Long> productIds = productResponses.stream().map(ProductResponse::getProductId).collect(Collectors.toList());
        assertThat(productIds).containsExactly(product1.getId(), product2.getId(), product3.getId());
    }

    @Test
    public void 특정_상품을_가져온다() {
        var response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products/{id}", product1.getId())
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ProductResponse productResponse = response.as(ProductResponse.class);
        assertThat(productResponse.getProductId()).isEqualTo(product1.getId());
        assertThat(productResponse.getPrice()).isEqualTo(product1.getPrice());
        assertThat(productResponse.getName()).isEqualTo(product1.getName());
        assertThat(productResponse.getImageUrl()).isEqualTo(product1.getImageUrl());
        assertThat(productResponse.getStock()).isEqualTo(product1.getStock());
    }

    @Test
    public void 상품을_추가한다() {
        var request = new ProductRequest("떡볶이", 5000, "http://example.com/tteokbboki.jpg", 30);

        var response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/products")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    public void 상품을_수정한다() {
        var request = new ProductRequest("새로운치킨", 15000, "http://example.com/tteokbboki.jpg", 30);

        var response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .put("/products/{id}", product1.getId())
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        Product findProduct = productDao.getProductById(product1.getId());
        assertThat(findProduct.getName()).isEqualTo("새로운치킨");
        assertThat(findProduct.getPrice()).isEqualTo(15000);
    }

}
