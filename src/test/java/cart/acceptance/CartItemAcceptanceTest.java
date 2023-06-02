package cart.acceptance;

import static cart.acceptance.CartItemSteps.장바구니_상품_수량_수정_요청;
import static cart.acceptance.CartItemSteps.장바구니에_담은_모든_상품_조회_요청;
import static cart.acceptance.CartItemSteps.장바구니에_상품_추가_요청;
import static cart.acceptance.CartItemSteps.장바구니에_상품_추가하고_아이디_반환;
import static cart.acceptance.CartItemSteps.장바구니에서_상품_삭제_요청;
import static cart.acceptance.CommonSteps.LOCATION_헤더를_검증한다;
import static cart.acceptance.CommonSteps.STATUS_CODE를_검증한다;
import static cart.acceptance.CommonSteps.권한_없음;
import static cart.acceptance.CommonSteps.인증_실패;
import static cart.acceptance.CommonSteps.정상_삭제;
import static cart.acceptance.CommonSteps.정상_생성;
import static cart.acceptance.CommonSteps.정상_처리;
import static cart.acceptance.MemberSteps.유저_생성_요청하고_유저_반환;
import static cart.acceptance.ProductSteps.상품_생성하고_아이디_반환;
import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.CartItemDto;
import cart.dto.CartItemRequest;
import cart.dto.ProductRequest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CartItemController 인수테스트")
@Sql(scripts = {"/delete.sql", "/schema.sql"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CartItemAcceptanceTest {

    @Autowired
    CartItemDao cartItemDao;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 장바구니에_상품을_추가한다() {
        // given
        var productRequest = new ProductRequest("떡볶이", 5000, "http://example.com/tteokbboki.jpg", 30);
        Member member = 유저_생성_요청하고_유저_반환("abc", "1234", 0);
        Long productId = 상품_생성하고_아이디_반환(productRequest);

        var cartItemRequest = new CartItemRequest(productId);

        // when
        var response = 장바구니에_상품_추가_요청(member, cartItemRequest);

        // then
        STATUS_CODE를_검증한다(response, 정상_생성);
        LOCATION_헤더를_검증한다(response);
    }

    @Test
    void 잘못된_사용자_정보로_장바구니에_상품_추가_요청시_실패한다() {
        // given
        var productRequest = new ProductRequest("떡볶이", 5000, "http://example.com/tteokbboki.jpg", 30);
        Member member = 유저_생성_요청하고_유저_반환("abc", "1234", 0);
        Long productId = 상품_생성하고_아이디_반환(productRequest);

        var cartItemRequest = new CartItemRequest(productId);

        // when
        Member illegalMember = new Member("def", "5678");
        var response = 장바구니에_상품_추가_요청(illegalMember, cartItemRequest);

        // then
        STATUS_CODE를_검증한다(response, 인증_실패);
    }

    @Test
    void 장바구니에_담긴_상품의_수량을_변경한다() {
        // given
        var productRequest = new ProductRequest("떡볶이", 5000, "http://example.com/tteokbboki.jpg", 30);
        Member member = 유저_생성_요청하고_유저_반환("abc", "1234", 0);
        Long productId = 상품_생성하고_아이디_반환(productRequest);

        var cartItemRequest = new CartItemRequest(productId);
        Long cartItemId = 장바구니에_상품_추가하고_아이디_반환(member, cartItemRequest);

        // when
        var response = 장바구니_상품_수량_수정_요청(member, cartItemId, 3);

        // then
        STATUS_CODE를_검증한다(response, 정상_처리);
        장바구니_상품_수량_수정_결과를_검증한다(cartItemId, 3);
    }

    @Test
    void 장바구니에_담긴_상품의_수량을_0으로_변경하면_장바구니에서_아이템이_삭제된다() {
        // given
        var productRequest = new ProductRequest("떡볶이", 5000, "http://example.com/tteokbboki.jpg", 30);
        Member member = 유저_생성_요청하고_유저_반환("abc", "1234", 0);
        Long productId = 상품_생성하고_아이디_반환(productRequest);

        var cartItemRequest = new CartItemRequest(productId);
        Long cartItemId = 장바구니에_상품_추가하고_아이디_반환(member, cartItemRequest);

        // when
        var response = 장바구니_상품_수량_수정_요청(member, cartItemId, 0);

        // then
        STATUS_CODE를_검증한다(response, 정상_처리);
        장바구니_상품_수량_수정_결과를_검증한다(cartItemId, 0);
    }

    @Test
    void 다른_사용자가_담은_장바구니_상품의_수량을_변경하면_실패한다() {
        // given
        var productRequest = new ProductRequest("떡볶이", 5000, "http://example.com/tteokbboki.jpg", 30);
        Member member1 = 유저_생성_요청하고_유저_반환("abc", "1234", 0);
        Member member2 = 유저_생성_요청하고_유저_반환("def", "1234", 0);
        Long productId = 상품_생성하고_아이디_반환(productRequest);

        var cartItemRequest = new CartItemRequest(productId);
        Long cartItemId = 장바구니에_상품_추가하고_아이디_반환(member1, cartItemRequest);

        // when
        var response = 장바구니_상품_수량_수정_요청(member2, cartItemId, 3);

        // then
        STATUS_CODE를_검증한다(response, 권한_없음);
    }

    @Test
    void 장바구니에_담긴_상품을_삭제한다() {
        // given
        var productRequest = new ProductRequest("떡볶이", 5000, "http://example.com/tteokbboki.jpg", 30);
        Member member = 유저_생성_요청하고_유저_반환("abc", "1234", 0);
        Long productId = 상품_생성하고_아이디_반환(productRequest);

        var cartItemRequest = new CartItemRequest(productId);
        Long cartItemId = 장바구니에_상품_추가하고_아이디_반환(member, cartItemRequest);

        // when
        var response = 장바구니에서_상품_삭제_요청(member, cartItemId);

        // then
        STATUS_CODE를_검증한다(response, 정상_삭제);
        장바구니_상품_삭제_결과를_검증한다(cartItemId);
    }

    @Test
    void 장바구니에_담긴_모든_상품을_조회한다() {
        // given
        var productRequest1 = new ProductRequest("떡볶이", 5000, "http://example.com/tteokbboki.jpg", 30);
        var productRequest2 = new ProductRequest("치킨", 5000, "http://example.com/chicken.jpg", 30);
        Member member = 유저_생성_요청하고_유저_반환("abc", "1234", 0);
        Long productId1 = 상품_생성하고_아이디_반환(productRequest1);
        Long productId2 = 상품_생성하고_아이디_반환(productRequest2);

        var cartItemRequest1 = new CartItemRequest(productId1);
        var cartItemRequest2 = new CartItemRequest(productId2);
        Long cartItemId1 = 장바구니에_상품_추가하고_아이디_반환(member, cartItemRequest1);
        Long cartItemId2 = 장바구니에_상품_추가하고_아이디_반환(member, cartItemRequest2);

        // when
        var response = 장바구니에_담은_모든_상품_조회_요청(member);

        // then
        STATUS_CODE를_검증한다(response, 정상_처리);
        장바구니의_모든_상품_조회_결과를_검증한다(response, List.of(cartItemId1, cartItemId2), List.of(productId1, productId2), List.of(1, 1));
    }

    private void 장바구니의_모든_상품_조회_결과를_검증한다(ExtractableResponse<Response> response,
                                         List<Long> cartItemIds, List<Long> productIds, List<Integer> quantitys) {
        List<CartItemDto> actualResponses = response.as(new TypeRef<>() {
        });
        for (int i = 0; i < actualResponses.size(); i++) {
            CartItemDto cartItemDto = actualResponses.get(i);
            assertThat(cartItemDto.getCartItemId()).isEqualTo(cartItemIds.get(i));
            assertThat(cartItemDto.getQuantity()).isEqualTo(quantitys.get(i));
            assertThat(cartItemDto.getProduct().getProductId()).isEqualTo(productIds.get(i));
        }
    }

    private void 장바구니_상품_수량_수정_결과를_검증한다(Long cartItemId, int expected) {
        CartItem cartItem = cartItemDao.findById(cartItemId);
        if (expected == 0) {
            assertThat(cartItem).isNull();
            return;
        }
        assertThat(cartItem.getQuantity()).isEqualTo(expected);
    }

    private void 장바구니_상품_삭제_결과를_검증한다(Long cartItemId) {
        assertThat(cartItemDao.findById(cartItemId)).isNull();
    }
}
