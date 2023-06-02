package cart.ui.api;

import cart.application.request.CreateCartItemRequest;
import cart.application.request.UpdateCartItemQuantityRequest;
import cart.config.ControllerTestConfig;
import cart.domain.cartitem.CartItem;
import cart.domain.member.Member;
import cart.domain.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

class CartItemApiControllerTest extends ControllerTestConfig {

    private static final String USERNAME = "a@a.com";
    private static final String PASSWORD = "1234";

    Product 상품_계란_등록() {
        return productDaoFixture.상품을_등록한다("계란", 1000);
    }

    Member 회원_등록() {
        return memberDaoFixture.회원을_등록한다("a@a.com", "1234", "10000", "1000");
    }

    CartItem 장바구니_등록(final Product 상품, final Member 회원) {
        return cartItemDaoFixture.장바구니_상품을_등록한다(상품, 회원, 1);
    }

    @Test
    void showCartItems() {
        final Product 계란 = 상품_계란_등록();
        final Member 회원 = 회원_등록();
        장바구니_등록(계란, 회원);

        given(spec)
                .log().all()
                .filter(document(DOCUMENT_IDENTIFIER,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("basic 64인코딩값")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("장바구니 상품 식별자값"),
                                fieldWithPath("[].quantity").description("장바구니 상품 수량"),
                                fieldWithPath("[].product.id").description("상품 식별자값"),
                                fieldWithPath("[].product.name").description("상품명"),
                                fieldWithPath("[].product.price").description("상품 가격"),
                                fieldWithPath("[].product.imageUrl").description("상품 이미지 주소")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
        .when()
                .log().all()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .get("/cart-items")
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void addCartItems() {
        final Product 계란 = 상품_계란_등록();
        회원_등록();

        given(spec)
                .log().all()
                .filter(document(DOCUMENT_IDENTIFIER,
                        requestHeaders(
                                headerWithName("Authorization").description("basic 64인코딩값")
                        ),
                        requestFields(
                                fieldWithPath("productId").description("상품 식별자값")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
        .when()
                .log().all()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .body(new CreateCartItemRequest(계란.getId()))
                .post("/cart-items")
        .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void updateCartItemQuantity() {
        final Product 계란 = 상품_계란_등록();
        final Member 회원 = 회원_등록();
        final CartItem 장바구니_상품 = 장바구니_등록(계란, 회원);

        given(spec)
                .log().all()
                .filter(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("id").description("장바구니 상품 식별자값")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("basic 64인코딩값")
                        ),
                        requestFields(
                                fieldWithPath("quantity").description("장바구니 상품 수량")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
        .when()
                .log().all()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .pathParam("id", 장바구니_상품.getId())
                .body(new UpdateCartItemQuantityRequest(10))
                .patch("/cart-items/{id}")
        .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void removeCartItems() {
        final Product 계란 = 상품_계란_등록();
        final Member 회원 = 회원_등록();
        final CartItem 장바구니_상품 = 장바구니_등록(계란, 회원);

        given(spec)
                .log().all()
                .filter(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("id").description("장바구니 상품 식별자값")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("basic 64인코딩값")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
        .when()
                .log().all()
                .auth().preemptive().basic(USERNAME, PASSWORD)
                .pathParam("id", 장바구니_상품.getId())
                .delete("/cart-items/{id}")
        .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
