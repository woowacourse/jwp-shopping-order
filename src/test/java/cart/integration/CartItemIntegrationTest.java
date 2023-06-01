package cart.integration;

import cart.dao.member.MemberDao;
import cart.dao.product.ProductDao;
import cart.domain.member.Member;
import cart.dto.cartitem.CartItemQuantityUpdateRequest;
import cart.dto.cartitem.CartItemRequest;
import cart.dto.cartitem.CartItemResponse;
import cart.dto.product.ProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static cart.fixture.MemberFixture.하디;
import static cart.fixture.MemberFixture.현구막;
import static cart.fixture.ProductFixture.치킨;
import static cart.fixture.ProductFixture.피자;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CartItemIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    private Long 치킨_아이디;
    private Long 피자_아이디;
    private Member 멤버_하디;
    private Member 멤버_현구막;

    @BeforeEach
    void setUp() {
        super.setUp();

        치킨_아이디 = productDao.createProduct(치킨);
        피자_아이디 = productDao.createProduct(피자);
        memberDao.addMember(하디);
        memberDao.addMember(현구막);
        멤버_하디 = memberDao.findMemberByEmail(하디.getEmail()).get();
        멤버_현구막 = memberDao.findMemberByEmail(현구막.getEmail()).get();
    }

    @Test
    void 장바구니에_아이템을_추가한다() {
        // given
        CartItemRequest cartItemRequest = new CartItemRequest(치킨_아이디);

        // when
        ExtractableResponse<Response> response = 장바구니에_아이템_추가(멤버_하디, cartItemRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 장바구니에_같은_상품을_두번_추가하면_실패한다() {
        // given
        CartItemRequest cartItemRequest = new CartItemRequest(치킨_아이디);

        // when
        ExtractableResponse<Response> firstResponse = 장바구니에_아이템_추가(멤버_하디, cartItemRequest);
        ExtractableResponse<Response> secondResponse = 장바구니에_아이템_추가(멤버_하디, cartItemRequest);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(firstResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        softAssertions.assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        softAssertions.assertAll();
    }

    @Test
    void 잘못된_멤버_정보로_장바구니에_아이템을_추가요청시_실패한다() {
        // given
        Member illegalMember = new Member(멤버_하디.getId(), 멤버_하디.getEmail(), 멤버_하디.getPassword() + "asdf");
        CartItemRequest cartItemRequest = new CartItemRequest(피자_아이디);

        // when
        ExtractableResponse<Response> response = 장바구니에_아이템_추가(illegalMember, cartItemRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 사용자의_장바구니를_조회한다() {
        // given
        장바구니에_아이템_추가(멤버_현구막, new CartItemRequest(피자_아이디));
        장바구니에_아이템_추가(멤버_현구막, new CartItemRequest(치킨_아이디));

        // when
        ExtractableResponse<Response> response = 회원의_전체_장바구니_조회(멤버_현구막);
        List<Long> resultCartItemIds = response.jsonPath().getList(".", CartItemResponse.class).stream()
                .map(CartItemResponse::getProduct)
                .map(ProductResponse::getId)
                .collect(Collectors.toList());

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        softAssertions.assertThat(resultCartItemIds.size()).isEqualTo(2);
        softAssertions.assertThat(resultCartItemIds).containsAll(List.of(피자_아이디, 치킨_아이디));
        softAssertions.assertAll();
    }

    @Test
    void 장바구니에_담긴_아이템의_수량을_변경한다() {
        // given
        ExtractableResponse<Response> addResponse = 장바구니에_아이템_추가(멤버_현구막, new CartItemRequest(피자_아이디));
        Long cartItemId = Long.parseLong(addResponse.header("Location").split("/")[2]);
        Long updateQuantity = 100L;

        // when
        ExtractableResponse<Response> response = 장바구니_아이템_수량_변경(멤버_현구막, cartItemId, updateQuantity);
        ExtractableResponse<Response> findResponse = 회원의_전체_장바구니_조회(멤버_현구막);
        List<CartItemResponse> cartItems = findResponse.jsonPath().getList(".", CartItemResponse.class);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        softAssertions.assertThat(cartItems.size()).isEqualTo(1);
        softAssertions.assertThat(cartItems.get(0).getQuantity()).isEqualTo(updateQuantity);
        softAssertions.assertAll();
    }

    @Test
    void 장바구니에_담긴_아이템의_수량을_0으로_변경하면_장바구니에서_아이템이_삭제된다() {
        // given
        ExtractableResponse<Response> addResponse = 장바구니에_아이템_추가(멤버_현구막, new CartItemRequest(피자_아이디));
        Long cartItemId = Long.parseLong(addResponse.header("Location").split("/")[2]);
        Long updateQuantity = 0L;

        // when
        ExtractableResponse<Response> response = 장바구니_아이템_수량_변경(멤버_현구막, cartItemId, updateQuantity);
        ExtractableResponse<Response> findResponse = 회원의_전체_장바구니_조회(멤버_현구막);
        List<CartItemResponse> cartItems = findResponse.jsonPath().getList(".", CartItemResponse.class);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        softAssertions.assertThat(cartItems).isEmpty();
        softAssertions.assertAll();
    }

    @Test
    void 다른_사용자가_담은_장바구니_아이템의_수량을_변경하려하면_실패한다() {
        // given
        ExtractableResponse<Response> addResponse = 장바구니에_아이템_추가(멤버_현구막, new CartItemRequest(피자_아이디));
        Long cartItemId = Long.parseLong(addResponse.header("Location").split("/")[2]);
        Long updateQuantity = 100L;

        // when
        ExtractableResponse<Response> response = 장바구니_아이템_수량_변경(멤버_하디, cartItemId, updateQuantity);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 장바구니에_담긴_아이템을_삭제한다() {
        // given
        ExtractableResponse<Response> addResponse = 장바구니에_아이템_추가(멤버_현구막, new CartItemRequest(피자_아이디));
        Long cartItemId = Long.parseLong(addResponse.header("Location").split("/")[2]);

        // when
        ExtractableResponse<Response> response = 장바구니_아이템_삭제(멤버_현구막, cartItemId);
        List<CartItemResponse> cartItems = 회원의_전체_장바구니_조회(멤버_현구막).jsonPath().getList(".", CartItemResponse.class);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        softAssertions.assertThat(cartItems).isEmpty();
        softAssertions.assertAll();
    }

    @Test
    void 다른_사용자가_담은_장바구니_아이템을_삭제하려하면_실패한다() {
        // given
        ExtractableResponse<Response> addResponse = 장바구니에_아이템_추가(멤버_현구막, new CartItemRequest(피자_아이디));
        Long cartItemId = Long.parseLong(addResponse.header("Location").split("/")[2]);
        Long updateQuantity = 100L;

        // when
        ExtractableResponse<Response> response = 장바구니_아이템_삭제(멤버_하디, cartItemId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 장바구니에_아이템_추가(Member member, CartItemRequest cartItemRequest) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(cartItemRequest)
                .when()
                .post("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 회원의_전체_장바구니_조회(Member member) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/cart-items")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 장바구니_아이템_수량_변경(Member member, Long cartItemId, long quantity) {
        CartItemQuantityUpdateRequest quantityUpdateRequest = new CartItemQuantityUpdateRequest(quantity);
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .body(quantityUpdateRequest)
                .patch("/cart-items/{cartItemId}", cartItemId)
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 장바구니_아이템_삭제(Member member, Long cartItemId) {
        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .delete("/cart-items/{cartItemId}", cartItemId)
                .then()
                .log().all()
                .extract();
    }
}
