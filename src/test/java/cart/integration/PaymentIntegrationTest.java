package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.*;
import cart.exception.AuthenticationException;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class PaymentIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Long productId;
    private Long productId2;
    private Long productId3;

    private List<Long> cartItemIds;
    private Member member;

    @BeforeEach
    void setUp(final RestDocumentationContextProvider restDocumentation) {

        super.setUp(restDocumentation);

        this.member = this.memberDao.getMemberById(1L).orElseThrow(AuthenticationException.NotFound::new);

        this.productId = this.createProduct(new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg"));
        this.productId2 = this.createProduct(new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg"));
        this.productId3 = this.createProduct(new ProductRequest("보쌈", 15_000, "http://example.com/pizza.jpg"));

        this.cartItemIds = List.of(this.requestAddCartItemAndGetId(this.member, this.productId),
                this.requestAddCartItemAndGetId(this.member, this.productId2), this.requestAddCartItemAndGetId(this.member, this.productId3));

        this.requestUpdateCartItemQuantity(this.member, this.cartItemIds.get(1), 2);
    }

    @DisplayName("선택한 장바구니 아이템의 예상 결제 금액을 조회한다")
    @Test
    void getPayment() {
        final ExtractableResponse<Response> response = given(this.spec).log().all()
                .queryParam("cartItemIds", this.cartItemIds)
                .filter(
                        document("get-payment",
                                requestParameters(parameterWithName("cartItemIds").description("선택한 장바구니의 아이템 id")),
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
                .auth().preemptive().basic(this.member.getEmail(), this.member.getPassword())
                .when()
                .get("/total-cart-price")
                .then()
                .log().all()
                .extract();

        final PaymentResponse responseBody = response.body().as(PaymentResponse.class);

        final PaymentResponse expected = new PaymentResponse(55_000,
                List.of(new DiscountResponse("5만원 이상 구매 시 10% 할인", 5_500)), 49_500, 3_500, 53_000);

        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(200),
                () -> assertThat(responseBody).usingRecursiveComparison().isEqualTo(expected)
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
