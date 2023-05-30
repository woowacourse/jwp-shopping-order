package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.*;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

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
    void setUp(final RestDocumentationContextProvider restDocumentation) {

        super.setUp(restDocumentation);

        this.member = this.memberDao.getMemberById(1L);

        this.productId = this.createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        this.productId2 = this.createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));
        this.productId3 = this.createProduct(new ProductRequest("보쌈", 15_000, "http://example.com/pizza.jpg"));

        this.cartItemIds = List.of(this.requestAddCartItemAndGetId(this.member, this.productId),
                this.requestAddCartItemAndGetId(this.member, this.productId2), this.requestAddCartItemAndGetId(this.member, this.productId3));

        this.orderItems = List.of(
                new OrderItemResponse("치킨", 1, "http://example.com/chicken.jpg", 10_000),
                new OrderItemResponse("피자", 2, "http://example.com/pizza.jpg", 30_000),
                new OrderItemResponse("보쌈", 1, "http://example.com/pizza.jpg", 15_000)
        );
        this.requestUpdateCartItemQuantity(this.member, this.cartItemIds.get(1), 2);
    }

    @Test
    @DisplayName("주문하기")
    void orderProducts() {
        final ExtractableResponse<Response> extractedResponse = given(this.spec).log().all()
                .filter(document("post-order",
                        requestFields(
                                fieldWithPath("cartItemIds").description("주문할 장바구니 항목들의 id 리스트")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderRequest(this.cartItemIds))
                .auth().preemptive().basic(this.member.getEmail(), this.member.getPassword())
                .when()
                .post("/orders")
                .then()
                .extract();

        assertThat(extractedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> postOrderAndReturnResponse() {
        return given(this.spec).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderRequest(this.cartItemIds))
                .auth().preemptive().basic(this.member.getEmail(), this.member.getPassword())
                .when()
                .post("/orders")
                .then()
                .extract();
    }

    @Test
    @DisplayName("주문 목록 조회하기")
    void getOrders() {

        final long orderId = this.getIdFromCreatedResponse(this.postOrderAndReturnResponse());
        final ExtractableResponse<Response> response = given(this.spec).log().all()
                .filter(document("get-orders",
                        responseFields(
                                fieldWithPath("[].id").description("주문 id"),
                                fieldWithPath("[].productList").description("주문한 상품들"),
                                fieldWithPath("[].productList[].name").description("상품 이름"),
                                fieldWithPath("[].productList[].quantity").description("상품 수량"),
                                fieldWithPath("[].productList[].imageUrl").description("상품 이미지 url"),
                                fieldWithPath("[].productList[].totalPrice").description("상품 가격"),
                                fieldWithPath("[].orderTime").description("주문 시간")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(this.member.getEmail(), this.member.getPassword())
                .when()
                .get("/orders")
                .then()
                .log().all()
                .extract();

        final List<OrderResponse> responseBody = response.body().jsonPath().getList(".", OrderResponse.class);

        final List<OrderResponse> expected = List.of(new OrderResponse(orderId, null, this.orderItems));

        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(responseBody).usingRecursiveComparison().ignoringFields("orderTime")
                        .isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("주문 상세 조회하기")
    void getOrderDetail() {
        final long orderId = this.getIdFromCreatedResponse(this.postOrderAndReturnResponse());

        final ExtractableResponse<Response> response = given(this.spec).log().all()
                .filter(document("get-order-detail",
                        pathParameters(
                                parameterWithName("orderId").description("주문 id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("주문 id"),
                                fieldWithPath("orderTime").description("주문 시간"),
                                fieldWithPath("productList").description("주문한 상품들"),
                                fieldWithPath("productList[].name").description("상품 이름"),
                                fieldWithPath("productList[].quantity").description("상품 수량"),
                                fieldWithPath("productList[].imageUrl").description("상품 이미지 url"),
                                fieldWithPath("productList[].totalPrice").description("상품 가격"),
                                fieldWithPath("paymentAmount").description("결제 정보"),
                                fieldWithPath("paymentAmount.originalPrice").description("주문 금액"),
                                fieldWithPath("paymentAmount.discounts").description("할인 정책 정보"),
                                fieldWithPath("paymentAmount.discounts[].discountPolicy").description("할인 정책"),
                                fieldWithPath("paymentAmount.discounts[].discountAmount").description("정책별 할인 금액"),
                                fieldWithPath("paymentAmount.discountedPrice").description("할인된 금액"),
                                fieldWithPath("paymentAmount.deliveryFee").description("배송비"),
                                fieldWithPath("paymentAmount.finalPrice").description("최종 결제 금액")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(this.member.getEmail(), this.member.getPassword())
                .when()
                .get("/orders/{orderId}", orderId)
                .then()
                .log().all()
                .extract();

        final OrderDetailResponse orderDetailResponseBody = response.body().as(OrderDetailResponse.class);
        final OrderDetailResponse expected = new OrderDetailResponse(this.orderItems, orderId, null, new PaymentResponse(55_000,
                List.of(new DiscountResponse("5만원 이상 구매 시 10% 할인", 5_500)), 49_500, 3_500, 53_000));
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(orderDetailResponseBody).usingRecursiveComparison().ignoringFields("orderTime")
                        .isEqualTo(expected)
        );
    }

    private Long createProduct(final ProductRequest productRequest) {
        final ExtractableResponse<Response> response = given(this.spec)
                .filter(document("create-product"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when()
                .post("/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return this.getIdFromCreatedResponse(response);
    }

    private long getIdFromCreatedResponse(final ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private ExtractableResponse<Response> requestAddCartItem(final Member member, final CartItemRequest cartItemRequest) {
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

    private Long requestAddCartItemAndGetId(final Member member, final Long productId) {
        final ExtractableResponse<Response> response = this.requestAddCartItem(member, new CartItemRequest(productId));
        return this.getIdFromCreatedResponse(response);
    }


    private ExtractableResponse<Response> requestUpdateCartItemQuantity(final Member member, final Long cartItemId, final int quantity) {
        final CartItemQuantityUpdateRequest quantityUpdateRequest = new CartItemQuantityUpdateRequest(quantity);
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
