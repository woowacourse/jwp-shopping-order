package cart.service.product;

import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.dto.sale.SaleProductRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
class ProductServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void init() {
        RestAssured.port = this.port;
    }

    @DisplayName("모든 상품을 찾는다.")
    @Test
    void find_all_products() {
        // given
        productService.createProduct(new ProductRequest("치킨", 10000, "img"));

        // when
        List<ProductResponse> result = productService.findAllProducts();

        // then
        assertAll(
                () -> assertThat(result.get(0).getId()).isEqualTo(1),
                () -> assertThat(result.get(0).getName()).isEqualTo("치킨")
        );
    }

    @DisplayName("상품을 하나 찾는다.")
    @Test
    void find_product_by_id() {
        // given
        productService.createProduct(new ProductRequest("치킨", 10000, "img"));

        // when
        ProductResponse result = productService.findProductById(1L);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1),
                () -> assertThat(result.getName()).isEqualTo("치킨")
        );
    }

    @DisplayName("상품을 저장한다.")
    @Test
    void create_product() {
        // when
        long id = productService.createProduct(new ProductRequest("치킨", 10000, "img"));
        ProductResponse result = productService.findProductById(1L);

        // then
        assertAll(
                () -> assertThat(id).isEqualTo(1),
                () -> assertThat(result.getId()).isEqualTo(1),
                () -> assertThat(result.getName()).isEqualTo("치킨")
        );
    }

    @DisplayName("상품을 업데이트한다.")
    @Test
    void update_product() {
        // given
        ProductRequest request = new ProductRequest("피자", 20000, "img2");
        long id = productService.createProduct(new ProductRequest("치킨", 10000, "img"));

        // when
        productService.updateProduct(id, request);
        ProductResponse result = productService.findProductById(id);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1),
                () -> assertThat(result.getName()).isEqualTo(request.getProductName())
        );
    }

    @DisplayName("상품에 할인을 적용한다.")
    @Test
    void apply_sale_on_product() {
        // given
        long id = productService.createProduct(new ProductRequest("치킨", 10000, "img"));
        SaleProductRequest req = new SaleProductRequest(10);

        // when
        productService.applySale(id, req);
        ProductResponse result = productService.findProductById(id);

        // then
        assertThat(result.getIsOnSale()).isEqualTo(true);
    }

    @DisplayName("상품에 할인을 제거한다.")
    @Test
    void un_apply_sale_on_product() {
        // given
        long id = productService.createProduct(new ProductRequest("치킨", 10000, "img"));
        SaleProductRequest req = new SaleProductRequest(10);
        productService.applySale(id, req);

        // when
        productService.unapplySale(id);
        ProductResponse result = productService.findProductById(id);

        // then
        assertThat(result.getIsOnSale()).isEqualTo(false);
    }
}
