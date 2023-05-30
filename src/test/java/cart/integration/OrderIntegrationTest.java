package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.DiscountResponse;
import cart.dto.OrderDetailResponse;
import cart.dto.OrderItemResponse;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.dto.PaymentResponse;
import cart.dto.ProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

public class OrderIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberDao memberDao;

    private Long productId;
    private Long productId2;
    private Long productId3;

    private List<Long> cartItemIds;

    private List<OrderItemResponse> orderItems;

    private Member member;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {

        super.setUp(restDocumentation);

        member = memberDao.getMemberById(1L);

        productId = createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));
        productId3 = createProduct(new ProductRequest("보쌈", 15_000, "http://example.com/pizza.jpg"));

        cartItemIds = List.of(requestAddCartItemAndGetId(member, productId),
                requestAddCartItemAndGetId(member, productId2), requestAddCartItemAndGetId(member, productId3));

        orderItems = List.of(
                new OrderItemResponse("치킨", 1, "http://example.com/chicken.jpg", 10_000),
                new OrderItemResponse("피자", 2, "http://example.com/pizza.jpg", 30_000),
                new OrderItemResponse("보쌈", 1, "http://example.com/pizza.jpg", 15_000)
        );
        requestUpdateCartItemQuantity(member, cartItemIds.get(1), 2);
    }

    @Test
    @DisplayName("주문하기")
    void orderProducts() {
        ExtractableResponse<Response> extractedResponse = postOrderAndReturnResponse();

        assertThat(extractedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> postOrderAndReturnResponse() {
        return given(this.spec).log().all()
                .filter(document("post-order",
                        requestFields(
                                fieldWithPath("cartItemIds").description("주문할 장바구니 항목들의 id 리스트")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderRequest(cartItemIds))
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .post("/orders")
                .then()
                .extract();
    }

    @Test
    @DisplayName("주문 목록 조회하기")
    void getOrders() {

        long orderId = getIdFromCreatedResponse(postOrderAndReturnResponse());

        ExtractableResponse<Response> response = given(this.spec).log().all()
                .filter(document("get-orders"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();

        List<OrderResponse> responseBody = response.body().jsonPath().getList(".", OrderResponse.class);

        List<OrderResponse> expected = List.of(new OrderResponse(orderId, null, orderItems));

        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(responseBody).usingRecursiveComparison().ignoringFields("orderTime")
                        .isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("주문 상세 조회하기")
    void getOrderDetail() {
        long orderId = getIdFromCreatedResponse(postOrderAndReturnResponse());

        ExtractableResponse<Response> response = given(this.spec).log().all()
                .filter(document("get-order-detail"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/orders/{orderId}", orderId)
                .then()
                .log().all()
                .extract();

        OrderDetailResponse orderDetailResponseBody = response.body().as(OrderDetailResponse.class);
        OrderDetailResponse expected = new OrderDetailResponse(orderItems, orderId, null, new PaymentResponse(55_000,
                List.of(new DiscountResponse("5만원 이상 구매 시 10% 할인", 5_500)), 49_500, 3_500, 53_000));
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(orderDetailResponseBody).usingRecursiveComparison().ignoringFields("orderTime")
                        .isEqualTo(expected)
        );
    }

    private Long createProduct(ProductRequest productRequest) {
        ExtractableResponse<Response> response = given(this.spec)
                .filter(document("create-product"))
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

    private ExtractableResponse<Response> requestAddCartItem(Member member, CartItemRequest cartItemRequest) {
        return given(this.spec).log().all()
                .filter(
                        document("add-cart-item",
                                requestFields(
                                        fieldWithPath("productId").description("추가할 상품 id")
                                )
                        )
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    private Long requestAddCartItemAndGetId(Member member, Long productId) {
        ExtractableResponse<Response> response = requestAddCartItem(member, new CartItemRequest(productId));
        return getIdFromCreatedResponse(response);
    }


    private ExtractableResponse<Response> requestUpdateCartItemQuantity(Member member, Long cartItemId, int quantity) {
        CartItemQuantityUpdateRequest quantityUpdateRequest = new CartItemQuantityUpdateRequest(quantity);
        return given(this.spec).log().all()
                .filter(
                        document("update-cart-item-quantity",
                                pathParameters(
                                        parameterWithName("cartItemId").description("수량을 변경할 장바구니 상품 id")
                                ),
                                requestFields(
                                        fieldWithPath("quantity").description("설정할 수량")
                                )
                        )
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .body(quantityUpdateRequest)
                .patch("/cart-items/{cartItemId}", cartItemId)
                .then()
                .log().all()
                .extract();
    }
}
