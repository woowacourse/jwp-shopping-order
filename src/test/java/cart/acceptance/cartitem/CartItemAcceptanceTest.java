package cart.acceptance.cartitem;

import static cart.acceptance.cartitem.CartItemSteps.예상_장바구니_상품_정보;
import static cart.acceptance.cartitem.CartItemSteps.장바구니_상품_수량_변경_요청;
import static cart.acceptance.cartitem.CartItemSteps.장바구니_상품_전체_조회_요청;
import static cart.acceptance.cartitem.CartItemSteps.장바구니_상품_정보_검증;
import static cart.acceptance.cartitem.CartItemSteps.장바구니_상품_제거_요청;
import static cart.acceptance.cartitem.CartItemSteps.장바구니_상품_추가_요청;
import static cart.acceptance.common.CommonAcceptanceSteps.권한없음;
import static cart.acceptance.common.CommonAcceptanceSteps.본문없음;
import static cart.acceptance.common.CommonAcceptanceSteps.생성된_ID;
import static cart.acceptance.common.CommonAcceptanceSteps.응답을_검증한다;
import static cart.acceptance.common.CommonAcceptanceSteps.인증되지않음;
import static cart.acceptance.common.CommonAcceptanceSteps.정상_생성;
import static cart.acceptance.common.CommonAcceptanceSteps.정상_요청;
import static cart.acceptance.product.ProductSteps.상품_생성_요청;
import static cart.acceptance.product.ProductSteps.예상_상품_정보;

import cart.acceptance.AcceptanceTest;
import cart.member.domain.Member;
import cart.member.infrastructure.persistence.dao.MemberDao;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CartItem 통합 테스트")
public class CartItemAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberDao memberDao;

    private Long productId;
    private Long productId2;
    private Member member;
    private Member member2;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        ExtractableResponse<Response> 응답1 = 상품_생성_요청("치킨", 10_000, "http://example.com/chicken.jpg");
        productId = 생성된_ID(응답1);

        ExtractableResponse<Response> 응답2 = 상품_생성_요청("피자", 15_000, "http://example.com/pizza.jpg");
        productId2 = 생성된_ID(응답2);

        memberDao.addMember(new Member(null, "email1", "1234"));
        memberDao.addMember(new Member(null, "email2", "1234"));

        member = memberDao.getMemberById(1L);
        member2 = memberDao.getMemberById(2L);
    }

    @Test
    void 장바구니에_상품을_추가한다() {
        var 응답 = 장바구니_상품_추가_요청(member, productId);

        응답을_검증한다(응답, 정상_생성);
    }

    @Test
    void 잘못된_회원이_장바구니에_상품_추가_요청시_실패한다() {
        Member 잘못된_회원 = new Member(member.getId(), member.getEmail(), member.getPassword() + "asdf");
        var 응답 = 장바구니_상품_추가_요청(잘못된_회원, productId);

        응답을_검증한다(응답, 인증되지않음);
    }

    @Test
    void 회원의_장바구니_상품을_조회한다() {
        var 응답1 = 장바구니_상품_추가_요청(member, productId);
        var 응답2 = 장바구니_상품_추가_요청(member, productId2);

        Long id1 = 생성된_ID(응답1);
        Long id2 = 생성된_ID(응답2);

        var 장바구니_상품_정보들 = 장바구니_상품_전체_조회_요청(member);

        var 예상_장바구니_상품_정보들 = List.of(
                예상_장바구니_상품_정보(id1, 1, 예상_상품_정보(productId, "치킨", 10_000, "http://example.com/chicken.jpg")),
                예상_장바구니_상품_정보(id2, 1, 예상_상품_정보(productId2, "피자", 15_000, "http://example.com/pizza.jpg"))
        );

        장바구니_상품_정보_검증(장바구니_상품_정보들, 예상_장바구니_상품_정보들);
    }

    @Test
    void 장바구니에_담긴_아이템의_수량을_변경한다() {
        Long 장바구니_상품_ID = 생성된_ID(
                장바구니_상품_추가_요청(member, productId)
        );

        var 응답 = 장바구니_상품_수량_변경_요청(member, 장바구니_상품_ID, 10);
        응답을_검증한다(응답, 정상_요청);

        var 장바구니_상품_정보들 = 장바구니_상품_전체_조회_요청(member);

        var 예상_장바구니_상품_정보들 = List.of(
                예상_장바구니_상품_정보(장바구니_상품_ID, 10, 예상_상품_정보(productId, "치킨", 10_000, "http://example.com/chicken.jpg"))
        );

        장바구니_상품_정보_검증(장바구니_상품_정보들, 예상_장바구니_상품_정보들);
    }

    @Test
    void 장바구니에_담긴_아이템의_수량을_0으로_변경하면_장바구니에서_아이템이_삭제된다() {
        Long 장바구니_상품_ID = 생성된_ID(
                장바구니_상품_추가_요청(member, productId)
        );

        var 응답 = 장바구니_상품_수량_변경_요청(member, 장바구니_상품_ID, 0);
        응답을_검증한다(응답, 정상_요청);

        var 장바구니_상품_정보들 = 장바구니_상품_전체_조회_요청(member);
        장바구니_상품_정보_검증(장바구니_상품_정보들, Collections.emptyList());
    }

    @Test
    void 다른_사용자가_담은_장바구니_아이템의_수량을_변경하려_하면_실패한다() {
        Long 장바구니_상품_ID = 생성된_ID(
                장바구니_상품_추가_요청(member, productId)
        );

        var 응답 = 장바구니_상품_수량_변경_요청(member2, 장바구니_상품_ID, 10);
        응답을_검증한다(응답, 권한없음);
    }

    @Test
    void 장바구니에_담긴_아이템을_삭제한다() {
        Long 장바구니_상품_ID = 생성된_ID(
                장바구니_상품_추가_요청(member, productId)
        );

        var 응답 = 장바구니_상품_제거_요청(member, 장바구니_상품_ID);
        응답을_검증한다(응답, 본문없음);

        var 장바구니_상품_정보들 = 장바구니_상품_전체_조회_요청(member);
        장바구니_상품_정보_검증(장바구니_상품_정보들, Collections.emptyList());
    }
}
