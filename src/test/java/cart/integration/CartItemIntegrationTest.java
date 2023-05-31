package cart.integration;

import static cart.fixture.JdbcTemplateFixture.insertMember;
import static cart.fixture.MemberFixture.MEMBER;
import static cart.integration.ProductIntegrationTest.상품을_추가하고_아이디를_반환;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Member;
import cart.dto.request.CartItemQuantityUpdateRequest;
import cart.dto.request.CartItemRequest;
import cart.dto.response.CartItemResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CartItemController 통합테스트 은(는)")
public class CartItemIntegrationTest extends IntegrationTest {

    private static final String API_URL = "/cart-items";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 장바구니에_상품을_추가한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        long 치킨_아이디 = 상품을_추가하고_아이디를_반환("치킨", 10_000, "www.naver.com");

        // when
        ExtractableResponse<Response> response = 장바구니_상품_추가(치킨_아이디, MEMBER);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotEmpty();
    }

    @Test
    void 장바구니에_담긴_모든_상품을_조회한다() {
        // given
        insertMember(MEMBER, jdbcTemplate);
        long 치킨_아이디 = 상품을_추가하고_아이디를_반환("치킨", 10_000, "www.naver.com");
        long 피자_아이디 = 상품을_추가하고_아이디를_반환("피자", 15_000, "www.kakao.com");
        장바구니_상품_추가(치킨_아이디, MEMBER);
        Long 장바구니_피자_아이디 = 장바구니에_상품_추가하고_아이디_반환(피자_아이디, MEMBER);
        장바구니_아이템_수량_변경(MEMBER, 장바구니_피자_아이디, 20);

        // when
        ExtractableResponse<Response> response = 장바구니_조회(MEMBER);

        // then
        List<CartItemResponse> result = response.as(
                new ParameterizedTypeReference<List<CartItemResponse>>() {
                }.getType()
        );
        CartItemResponse 첫번째아이템 = result.get(0);
        CartItemResponse 두번째아이템 = result.get(1);
        아이템_검증(첫번째아이템, "치킨", 10_000, "www.naver.com", 1);
        아이템_검증(두번째아이템, "피자", 15_000, "www.kakao.com", 20);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void 장바구니_상품_수량을_변경한다() {
        // given
        insertMember(MEMBER,jdbcTemplate);
        long 치킨_아이디 = 상품을_추가하고_아이디를_반환("치킨", 10_000, "www.naver.com");
        Long 장바구니_치킨_아이디 = 장바구니에_상품_추가하고_아이디_반환(치킨_아이디, MEMBER);

        // when
        ExtractableResponse<Response> response = 장바구니_아이템_수량_변경(MEMBER, 장바구니_치킨_아이디, 20);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<CartItemResponse> cartItemResponses = 장바구니_조회하고_리스트_반환(MEMBER);
        아이템_검증(cartItemResponses.get(0), "치킨", 10_000, "www.naver.com", 20);
    }

    @Test
    void 장바구니_상품을_삭제한다() {
        // given
        insertMember(MEMBER,jdbcTemplate);
        long 치킨_아이디 = 상품을_추가하고_아이디를_반환("치킨", 10_000, "www.naver.com");
        Long 장바구니_치킨_아이디 = 장바구니에_상품_추가하고_아이디_반환(치킨_아이디, MEMBER);

        // when
        ExtractableResponse<Response> response = 장바구니_상품_삭제(장바구니_치킨_아이디, MEMBER);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        List<CartItemResponse> cartItemResponses = 장바구니_조회하고_리스트_반환(MEMBER);
        assertThat(cartItemResponses.size()).isEqualTo(0);
    }


    private void 아이템_검증(CartItemResponse 아이템, String 이름, int 가격, String 이미지_URL, int 수량) {
        assertAll(
                () -> assertThat(아이템.getProduct().getName()).isEqualTo(이름),
                () -> assertThat(아이템.getProduct().getPrice()).isEqualTo(가격),
                () -> assertThat(아이템.getProduct().getImageUrl()).isEqualTo(이미지_URL),
                () -> assertThat(아이템.getQuantity()).isEqualTo(수량)
        );
    }

    private Long 장바구니에_상품_추가하고_아이디_반환(Long 상품아이디, Member 회원) {
        ExtractableResponse<Response> response = 장바구니_상품_추가(상품아이디, 회원);
        final String location = response.header("location");
        final String id = location.substring(location.lastIndexOf("/") + 1);
        return Long.parseLong(id);
    }

    private ExtractableResponse<Response> 장바구니_상품_추가(long 상품아이디, Member 회원) {
        var 요청 = new CartItemRequest(상품아이디);
        return given().log().all()
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword())
                .contentType(JSON)
                .body(요청)
                .when()
                .post(API_URL)
                .then()
                .log().all()
                .extract();
    }

    private List<CartItemResponse> 장바구니_조회하고_리스트_반환(Member member) {
        ExtractableResponse<Response> response = 장바구니_조회(MEMBER);
        return response.as(new ParameterizedTypeReference<List<CartItemResponse>>() {
                }.getType()
        );
    }

    private ExtractableResponse<Response> 장바구니_조회(Member member) {
        return given().log().all()
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get(API_URL)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 장바구니_아이템_수량_변경(Member 회원, Long 장바구니_아이템_아이디, int 수량) {
        var 요청 = new CartItemQuantityUpdateRequest(수량);
        return given().log().all()
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword())
                .contentType(JSON)
                .body(요청)
                .when()
                .patch(API_URL + "/{id}", 장바구니_아이템_아이디)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 장바구니_상품_삭제(Long 장바구니_아이템_아이디, Member 회원) {
        return given().log().all()
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword())
                .when()
                .delete(API_URL + "/{id}", 장바구니_아이템_아이디)
                .then()
                .log().all()
                .extract();
    }
}
