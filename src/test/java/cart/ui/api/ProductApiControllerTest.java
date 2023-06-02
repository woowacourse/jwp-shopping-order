package cart.ui.api;

import cart.application.request.CreateProductRequest;
import cart.config.ControllerTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class ProductApiControllerTest extends ControllerTestConfig {

    Long 상품_계란_등록() {
        return productDaoFixture.상품을_등록한다("계란", 1000).getId();
    }

    @Test
    void getAllProducts() {
        상품_계란_등록();

        given(spec)
                .log().all()
                .filter(document("{method-name}",
                        responseFields(
                                fieldWithPath("[].id").description("상품 식별자값"),
                                fieldWithPath("[].name").description("상품명"),
                                fieldWithPath("[].price").description("상품 가격"),
                                fieldWithPath("[].imageUrl").description("상품 이미지 주소")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
        .when()
                .get("/products")
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getProductById() {
        상품_계란_등록();

        given(spec)
                .log().all()
                .filter(document("{method-name}",
                        responseFields(
                                fieldWithPath("id").description("상품 식별자값"),
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("price").description("상품 가격"),
                                fieldWithPath("imageUrl").description("상품 이미지 주소")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
        .when()
                .pathParam("id", 1L)
                .get("/products/{id}")
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void createProduct() {
        given(spec)
                .log().all()
                .filter(document("{method-name}",
                        requestFields(
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("price").description("상품 가격"),
                                fieldWithPath("imageUrl").description("상품 이미지 주소")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
        .when()
                .body(new CreateProductRequest("계란", 1000, "https://계란_이미지_주소.png"))
                .post("/products")
        .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void updateProduct() {
        final Long 상품_계란_식별자값 = 상품_계란_등록();

        given(spec)
                .log().all()
                .filter(document("{method-name}",
                        requestFields(
                                fieldWithPath("name").description("상품명"),
                                fieldWithPath("price").description("상품 가격"),
                                fieldWithPath("imageUrl").description("상품 이미지 주소")
                        )))
                .contentType(APPLICATION_JSON_VALUE)
        .when()
                .body(new CreateProductRequest("수정된 계란", 1000, "https://계란_이미지_주소.png"))
                .pathParam("id", 상품_계란_식별자값)
                .put("/products/{id}")
        .then()
                .statusCode(HttpStatus.OK.value());
    }
}
