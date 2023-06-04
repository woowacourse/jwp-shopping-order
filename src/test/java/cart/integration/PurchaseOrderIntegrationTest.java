package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.dto.purchaseorder.PurchaseOrderItemInfoResponse;
import cart.dto.purchaseorder.PurchaseOrderItemRequest;
import cart.dto.purchaseorder.PurchaseOrderRequest;
import cart.dto.purchaseorder.response.PurchaseOrderItemResponse;
import cart.dto.purchaseorder.response.PurchaseOrderPageResponse;
import cart.dto.purchaseorder.response.PurchaseOrderResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static cart.TestFeatures.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.*;

public class PurchaseOrderIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();
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
        return Long.parseLong(response.header("Location")
                                      .split("/")[2]);
    }

    private ExtractableResponse<Response> requestAddPurchaseOrder(Member member, PurchaseOrderRequest purchaseOrderRequest) {
        return given().log()
                      .all()
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .auth()
                      .preemptive()
                      .basic(member.getEmail(), member.getPassword())
                      .body(purchaseOrderRequest)
                      .when()
                      .post("/orders")
                      .then()
                      .log()
                      .all()
                      .extract();
    }

    @DisplayName("특정 회원의 특정 페이지의 주문 목록을 조회한다")
    @Test
    void showPurchaseOrders() {
        List<PurchaseOrderItemInfoResponse> expectInfoResponse = List.of(
                new PurchaseOrderItemInfoResponse(회원1_주문1.getId(), 회원1_주문1.getPayment(), 회원1_주문1.getOrderAt(), 주문1_상품1.getProduct()
                                                                                                                      .getName(), 주문1_상품1.getProduct()
                                                                                                                                         .getImageUrl(), 2),
                new PurchaseOrderItemInfoResponse(회원1_주문2.getId(), 회원1_주문2.getPayment(), 회원1_주문2.getOrderAt(), 주문2_상품1.getProduct()
                                                                                                                      .getName(), 주문2_상품1.getProduct()
                                                                                                                                         .getImageUrl(), 2),
                new PurchaseOrderItemInfoResponse(회원1_주문3.getId(), 회원1_주문3.getPayment(), 회원1_주문3.getOrderAt(), 주문3_상품1.getProduct()
                                                                                                                      .getName(), 주문3_상품1.getProduct()
                                                                                                                                         .getImageUrl(), 2)
        );
        PurchaseOrderPageResponse expectPageResponse = new PurchaseOrderPageResponse(1, 1, 3, expectInfoResponse);

        PurchaseOrderPageResponse response = requestShowPurchaseOrders(member, 1);

        assertThat(response).isEqualTo(expectPageResponse);
    }

    private PurchaseOrderPageResponse requestShowPurchaseOrders(Member member, int page) {
        return given().log()
                      .all()
                      .auth()
                      .preemptive()
                      .basic(member.getEmail(), member.getPassword())
                      .when()
                      .get(String.format("/orders?page=%d", page))
                      .then()
                      .log()
                      .all()
                      .statusCode(HttpStatus.OK.value())
                      .extract()
                      .jsonPath()
                      .getObject(".", PurchaseOrderPageResponse.class);
    }

    @DisplayName("특정 회원의 특정 페이지의 주문 목록을 조회한다")
    @Test
    void showPurchaseOrder() {
        List<PurchaseOrderItemResponse> expectItemResponse = List.of(
                new PurchaseOrderItemResponse(주문1_상품1.getQuantity(), ProductResponse.of(주문1_상품1.getProduct())),
                new PurchaseOrderItemResponse(주문1_상품2.getQuantity(), ProductResponse.of(주문1_상품2.getProduct()))
        );
        PurchaseOrderResponse expectResponse = new PurchaseOrderResponse(회원1_주문1.getId(), 회원1_주문1.getOrderAt(),
                회원1_주문1.getPayment(), 회원1_주문1.getUsedPoint(), 500, expectItemResponse);

        PurchaseOrderResponse response = requestShowPurchaseOrder(1L);

        assertThat(response).isEqualTo(expectResponse);
    }

    private PurchaseOrderResponse requestShowPurchaseOrder(Long orderId) {
        return given().log()
                      .all()
                      .auth()
                      .preemptive()
                      .basic(member.getEmail(), member.getPassword())
                      .when()
                      .get(String.format(String.format("/orders/%d", orderId)))
                      .then()
                      .log()
                      .all()
                      .statusCode(HttpStatus.OK.value())
                      .extract()
                      .jsonPath()
                      .getObject(".", PurchaseOrderResponse.class);
    }
}
