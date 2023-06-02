package cart.ui;

import cart.config.ControllerTestConfig;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.OrderRequest;
import cart.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;

import static cart.fixture.MemberFixture.다니;
import static cart.fixture.ProductFixture.치킨;
import static cart.fixture.ProductFixture.피자;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderApiControllerTest extends ControllerTestConfig {

    private static final String DOCUMENT_IDENTIFIER = "{method-name}";

    Product 상품_등록(Product product) {
        Long 상품_ID = productRepository.create(product);
        return productRepository.findById(상품_ID).orElseGet(null);
    }

    Member 회원_등록() {
        Long 회원_ID = memberRepository.create(다니.MEMBER);
        return memberRepository.findById(회원_ID).orElseGet(null);
    }

    CartItem 장바구니_등록(final Product 상품, final Member 회원) {
        final Long 장바구니_상품_ID = cartItemRepository.create(new CartItem(회원, 상품));
        return cartItemRepository.findById(장바구니_상품_ID).orElseGet(null);
    }

    @Test
    void showOrderedItems() {
        final Product 피자 = 상품_등록(ProductFixture.피자.PRODUCT);
        final Member 회원 = 회원_등록();
        장바구니_등록(피자, 회원);

        given(spec)
                .log().all()
                .filter(document(DOCUMENT_IDENTIFIER,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("basic 64인코딩값")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .log().all()
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword())
                .get("/orders")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void addOrder() {
        final Product 상품 = 상품_등록(피자.PRODUCT);
        final Product 상품2 = 상품_등록(치킨.PRODUCT);
        final Member 회원 = 회원_등록();
        final CartItem 장바구니_상품 = 장바구니_등록(상품, 회원);
        final CartItem 장바구니_상품2 = 장바구니_등록(상품2, 회원);

        List<Long> 주문할_상품들_id = List.of(장바구니_상품.getId(), 장바구니_상품2.getId());

        given(spec)
                .log().all()
                .filter(document(DOCUMENT_IDENTIFIER,
                        requestHeaders(
                                headerWithName("Authorization").description("basic 64인코딩값")
                        ),
                        requestFields(
                                fieldWithPath("cartIds").description("상품 식별자값들"),
                                fieldWithPath("point").description("사용할 포인트")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .log().all()
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword())
                .body(new OrderRequest(주문할_상품들_id, 0))
                .post("/orders")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void updateCartItemQuantity() {
        final Product 상품 = 상품_등록(피자.PRODUCT);
        final Member 회원 = 회원_등록();
        final CartItem 장바구니_상품 = 장바구니_등록(상품, 회원);

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
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword())
                .pathParam("id", 장바구니_상품.getId())
                .body(new CartItemQuantityUpdateRequest(10))
                .patch("/cart-items/{id}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void removeCartItems() {
        final Product 상품 = 상품_등록(피자.PRODUCT);
        final Member 회원 = 회원_등록();
        final CartItem 장바구니_상품 = 장바구니_등록(상품, 회원);

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
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword())
                .pathParam("id", 장바구니_상품.getId())
                .delete("/cart-items/{id}")
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
