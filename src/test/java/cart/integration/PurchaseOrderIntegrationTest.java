package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.ProductRequest;
import cart.dto.PurchaseOrderItemRequest;
import cart.dto.PurchaseOrderRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.*;

@Sql({"/schema.sql", "/data.sql"})
public class PurchaseOrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Long productId;
    private Long productId2;
    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();
        productId = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));

        productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));

        member = memberDao.getMemberById(1L);
    }

    @DisplayName("상품을 주문하면 포인트 적립 및 주문이 추가된다")
    @Test
    void addPurchaseOrder() {
        List<PurchaseOrderItemRequest> purchaseOrderItemRequests = List.of(
                new PurchaseOrderItemRequest(1L, 2),
                new PurchaseOrderItemRequest(2L, 4)
        );
        PurchaseOrderRequest purchaseOrderRequest = new PurchaseOrderRequest(1000, purchaseOrderItemRequests);

        ExtractableResponse<Response> response = requestAddPurchaseOrder(member, purchaseOrderRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("상품을 주문 중 문제가 발생하면 BadRequest를 반환한다")
    @Test
    void addPurchaseOrderException() {
        List<PurchaseOrderItemRequest> purchaseOrderItemRequests = List.of(
                new PurchaseOrderItemRequest(1L, 2),
                new PurchaseOrderItemRequest(2L, 4)
        );
        PurchaseOrderRequest purchaseOrderRequest = new PurchaseOrderRequest(100000, purchaseOrderItemRequests);

        ExtractableResponse<Response> response = requestAddPurchaseOrder(member, purchaseOrderRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private Long createProduct(ProductRequest productRequest) {
        ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return getIdFromCreatedResponse(response);
    }

    private long getIdFromCreatedResponse(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private ExtractableResponse<Response> requestAddPurchaseOrder(Member member, PurchaseOrderRequest purchaseOrderRequest) {
        return given().log().all()
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .auth().preemptive().basic(member.getEmail(), member.getPassword())
                      .body(purchaseOrderRequest)
                      .when()
                      .post("/orders")
                      .then()
                      .log().all()
                      .extract();
    }
}
