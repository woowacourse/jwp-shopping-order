package cart.integration;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.HomePagingRequest;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.fixtures.MemberFixtures;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static cart.fixtures.MemberFixtures.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class ProductIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    private Member member;

    @BeforeEach
    void setUp() {
        super.setUp();

        member = memberDao.getMemberById(1L);
    }

    @Test
    void 모든_상품을_조회하다() {
        final ExtractableResponse<Response> result = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products")
                .then()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 특정_상품을_조회하다() {
        final ExtractableResponse<Response> result = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/products/" + 1L)
                .then()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 상품_목록을_원하는_구간만큼_조회하다() {
        final HomePagingRequest request = new HomePagingRequest(4L, 2);
        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .get("/products/cart-items")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 멤버와_상품_id를_통해_장바구니를_가져오다() {
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/products/" + 1L + "/cart-items")
                .then()
                .extract();

        System.out.println(member.getEmail());
        System.out.println(member.getPassword());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 상품을_추가하다() {
        final ProductRequest request = new ProductRequest("치킨", 10_000, "http://example.com/chicken.jpg");

        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/products")
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 상품을_수정하다() {
        final ProductRequest product = new ProductRequest("피자", 15_000, "http://example.com/pizza.jpg");

        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when()
                .put("/products/" + 1L)
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 상품을_삭제하다() {
        final ExtractableResponse<Response> response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/products/" + 1L)
                .then()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
