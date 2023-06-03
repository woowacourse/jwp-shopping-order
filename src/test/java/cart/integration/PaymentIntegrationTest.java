package cart.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import cart.dto.DiscountResponse;
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

public class PaymentIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Long productId;
    private Long productId2;
    private Long productId3;

    private List<Long> cartItemIds;
    private Member member;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {

        super.setUp(restDocumentation);

        member = memberDao.getMemberById(1L);

        productId = createProduct(
            new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        productId2 = createProduct(
            new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));
        productId3 = createProduct(
            new ProductRequest("보쌈", 15_000, "http://example.com/pizza.jpg"));

        cartItemIds = List.of(requestAddCartItemAndGetId(member, productId),
            requestAddCartItemAndGetId(member, productId2),
            requestAddCartItemAndGetId(member, productId3));

        requestUpdateCartItemQuantity(member, cartItemIds.get(1), 2);
    }

    @DisplayName("선택한 장바구니 아이템의 예상 결제 금액을 조회한다")
    @Test
    void getPayment() {
        ExtractableResponse<Response> response = given(this.spec).log().all()
            .queryParam("cartItemIds", cartItemIds)
            .filter(
                document("get-payment",
                    requestParameters(
                        parameterWithName("cartItemIds").description("선택한 장바구니의 아이템 id")),
                    responseFields(
                        fieldWithPath("originalPrice").description("주문 금액"),
                        fieldWithPath("discounts[].discountPolicy").description("적용된 할인 정책 이름"),
                        fieldWithPath("discounts[].discountAmount").description("할인 금액"),
                        fieldWithPath("discountedPrice").description("할인 적용 금액"),
                        fieldWithPath("deliveryFee").description("배송비"),
                        fieldWithPath("finalPrice").description("예상 결제 금액")
                    )
                )
            )
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().preemptive().basic(member.getEmail(), member.getPassword())
            .when()
            .get("/total-cart-price")
            .then()
            .log().all()
            .extract();

        PaymentResponse responseBody = response.body().as(PaymentResponse.class);

        PaymentResponse expected = new PaymentResponse(55_000,
            List.of(new DiscountResponse("5만원 이상 구매 시 10% 할인", 5_500)), 49_500, 3_500, 53_000);

        Assertions.assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(200),
            () -> assertThat(responseBody).usingRecursiveComparison().isEqualTo(expected)
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

    private ExtractableResponse<Response> requestAddCartItem(Member member,
        CartItemRequest cartItemRequest) {
        return given(this.spec).log().all()
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
        ExtractableResponse<Response> response = requestAddCartItem(member,
            new CartItemRequest(productId));
        return getIdFromCreatedResponse(response);
    }


    private ExtractableResponse<Response> requestUpdateCartItemQuantity(Member member,
        Long cartItemId, int quantity) {
        CartItemQuantityUpdateRequest quantityUpdateRequest = new CartItemQuantityUpdateRequest(
            quantity);
        return given(this.spec).log().all()
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
