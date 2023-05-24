package cart.ui;

import cart.config.ControllerTestConfig;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.dto.CartItemQuantityUpdateRequest;
import cart.dto.CartItemRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class CartItemApiControllerTest extends ControllerTestConfig {
    Product 상품_계란_등록() {
        final Product product = new Product("계란", 1000, "계란 이미지 주소");
        final Long 상품_계란_식별자값 = productDao.createProduct(product);
        return new Product(상품_계란_식별자값, product.getName(), product.getPrice(), product.getImageUrl());
    }

    Member 회원_등록() {
        final Member member = new Member(1L, "test@test.com", "test");
        memberDao.addMember(member);
        return member;
    }

    CartItem 장바구니_등록(final Product 상품, final Member 회원) {
        final Long 장바구니_상품_식별자값 = cartItemDao.save(new CartItem(회원, 상품));
        return new CartItem(장바구니_상품_식별자값, 1, 상품, 회원);
    }

    @Test
    void showCartItems() {
        final Product 계란 = 상품_계란_등록();
        final Member 회원 = 회원_등록();
        장바구니_등록(계란, 회원);

        given(spec)
                .log().all()
                .header("Authorization", "basic dGVzdEB0ZXN0LmNvbTp0ZXN0")
                .filter(document("{method-name}",
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
                .get("/cart-items")
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void addCartItems() {
        final Product 계란 = 상품_계란_등록();
        회원_등록();

        given(spec)
                .log().all()
                .header("Authorization", "basic dGVzdEB0ZXN0LmNvbTp0ZXN0")
                .body(new CartItemRequest(계란.getId()))
                .filter(document("{method-name}",
                        requestFields(
                                fieldWithPath("productId").description("상품 식별자값")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
        .when()
                .post("/cart-items")
        .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void updateCartItemQuantity() {
        final Product 계란 = 상품_계란_등록();
        final Member 회원 = 회원_등록();
        final CartItem 장바구니_상품 = 장바구니_등록(계란, 회원);

        given(spec)
                .log().all()
                .header("Authorization", "basic dGVzdEB0ZXN0LmNvbTp0ZXN0")
                .pathParam("id", 장바구니_상품.getId())
                .body(new CartItemQuantityUpdateRequest(10))
                .filter(document("{method-name}",
                        requestFields(
                                fieldWithPath("quantity").description("장바구니 상품 수량")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
        .when()
                .patch("/cart-items/{id}")
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void removeCartItems() {
        final Product 계란 = 상품_계란_등록();
        final Member 회원 = 회원_등록();
        final CartItem 장바구니_상품 = 장바구니_등록(계란, 회원);

        given(spec)
                .log().all()
                .header("Authorization", "basic dGVzdEB0ZXN0LmNvbTp0ZXN0")
                .pathParam("id", 장바구니_상품.getId())
                .filter(document("{method-name}"))
                .contentType(APPLICATION_JSON_VALUE)
        .when()
                .delete("/cart-items/{id}")
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
