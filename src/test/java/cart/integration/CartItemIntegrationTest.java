package cart.integration;

import static cart.integration.steps.CartItemStep.장바구니_상품_삭제_요청;
import static cart.integration.steps.CartItemStep.장바구니_상품_수정_요청;
import static cart.integration.steps.CartItemStep.장바구니_상품_조회_요청;
import static cart.integration.steps.CartItemStep.장바구니_상품_추가_요청;
import static cart.integration.steps.CommonStep.헤더_ID_값_파싱;
import static cart.integration.steps.ProductStep.상품_생성_요청_생성;
import static cart.integration.steps.ProductStep.상품_생성_요청후_상품_ID를_리턴한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import cart.dto.CartItemResponse;
import cart.exception.ErrorMessage;
import cart.repository.MemberRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class CartItemIntegrationTest extends IntegrationTest {

    private final MemberEntity 멤버_엔티티 = new MemberEntity
            (null, "vero@email", "asdf1234", 20000, null, null);
    private final MemberEntity 다른_멤버_엔티티 = new MemberEntity
            (null, "otheruser@email", "asdf1234", 3000, null, null);
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberDao memberDao;

    @Test
    void 장바구니에_상품을_추가한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        Long 치킨_ID = 상품_생성_요청후_상품_ID를_리턴한다(상품_생성_요청_생성("치킨", 10_000, "http://example.com/chicken.jpg"));

        // when
        var 응답 = 장바구니_상품_추가_요청(멤버, 치킨_ID);
        long 추가된_장바구니_상품_ID = 헤더_ID_값_파싱(응답);

        // then
        assertAll(
                () -> assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(응답.header(HttpHeaders.LOCATION)).isEqualTo("/cart-items/" + 추가된_장바구니_상품_ID)
        );
    }

    @Test
    void 잘못된_사용자_정보로_장바구니에_상품을_추가_요청시_실패한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        Long 치킨_ID = 상품_생성_요청후_상품_ID를_리턴한다(상품_생성_요청_생성("치킨", 10_000, "http://example.com/chicken.jpg"));
        Member 잘못된_멤버 = new Member(멤버.getId(), 멤버.getEmail(), 멤버.getPassword() + "이상한비밀번호", 0);

        // when
        var 응답 = 장바구니_상품_추가_요청(잘못된_멤버, 치킨_ID);

        // then
        assertAll(
                () -> assertThat(응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value()),
                () -> assertThat(응답.jsonPath().getString("message"))
                        .isEqualTo(ErrorMessage.UNAUTHORIZED_MEMBER.getMessage())
        );
    }

    @Test
    void 사용자가_담은_장바구니_상품을_조회한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);

        Long 치킨_ID = 상품_생성_요청후_상품_ID를_리턴한다(상품_생성_요청_생성("치킨", 10_000, "http://example.com/chicken.jpg"));
        Long 피자_ID = 상품_생성_요청후_상품_ID를_리턴한다(상품_생성_요청_생성("피자", 20_000, "http://example.com/pizza.jpg"));

        Long 첫번째_장바구니_상품 = 장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다(멤버, 치킨_ID);
        Long 두번째_장바구니_상품 = 장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다(멤버, 피자_ID);

        // when
        var 응답 = 장바구니_상품_조회_요청(멤버);

        // then
        assertAll(
                () -> assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(응답.jsonPath().getList(".", CartItemResponse.class))
                        .extracting(CartItemResponse::getId)
                        .containsExactly(첫번째_장바구니_상품, 두번째_장바구니_상품)
        );
    }

    @Test
    void 장바구니에_담긴_상품의_수량을_변경한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);

        Long 치킨_ID = 상품_생성_요청후_상품_ID를_리턴한다(상품_생성_요청_생성("치킨", 10_000, "http://example.com/chicken.jpg"));
        Long 첫번째_장바구니_상품_ID = 장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다(멤버, 치킨_ID);

        // when
        var 수정_응답 = 장바구니_상품_수정_요청(멤버, 첫번째_장바구니_상품_ID, 10);
        var 조회_응답 = 장바구니_상품_조회_요청(멤버);

        // then
        assertAll(
                () -> assertThat(수정_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(조회_응답.jsonPath().getList(".", CartItemResponse.class))
                        .extracting(CartItemResponse::getQuantity)
                        .containsExactly(10)
        );
    }

    @Test
    void 장바구니에_담긴_상품의_수량을_0으로_변경하면_장바구니에서_상품이_삭제된다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);

        Long 치킨_ID = 상품_생성_요청후_상품_ID를_리턴한다(상품_생성_요청_생성("치킨", 10_000, "http://example.com/chicken.jpg"));
        Long 첫번째_장바구니_상품_ID = 장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다(멤버, 치킨_ID);

        장바구니_상품_수정_요청(멤버, 첫번째_장바구니_상품_ID, 0);

        // when
        var 응답 = 장바구니_상품_조회_요청(멤버);

        // then
        assertThat(응답.jsonPath().getList(".", CartItemResponse.class)).isEmpty();
    }

    @Test
    void 다른_사용자가_담은_장바구니_상품의_수량을_변경하려_하면_실패한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);

        Long 치킨_ID = 상품_생성_요청후_상품_ID를_리턴한다(상품_생성_요청_생성("치킨", 10_000, "http://example.com/chicken.jpg"));
        Long 첫번째_장바구니_상품_ID = 장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다(멤버, 치킨_ID);

        // when
        Member 다른_멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(다른_멤버_엔티티);
        var 응답 = 장바구니_상품_수정_요청(다른_멤버, 첫번째_장바구니_상품_ID, 10);

        // then
        assertAll(
                () -> assertThat(응답.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
                () -> assertThat(응답.jsonPath().getString("message"))
                        .isEqualTo(ErrorMessage.INVALID_CART_ITEM_OWNER.getMessage())
        );
    }

    @Test
    void 장바구니에_담긴_아이템을_삭제한다() {
        // given
        Member 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);

        Long 치킨_ID = 상품_생성_요청후_상품_ID를_리턴한다(상품_생성_요청_생성("치킨", 10_000, "http://example.com/chicken.jpg"));
        Long 첫번째_장바구니_상품_ID = 장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다(멤버, 치킨_ID);

        // when
        var 삭제_응답 = 장바구니_상품_삭제_요청(멤버, 첫번째_장바구니_상품_ID);
        var 조회_응답 = 장바구니_상품_조회_요청(멤버);

        // then
        assertAll(
                () -> assertThat(삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(조회_응답.jsonPath().getList(".", CartItemResponse.class)).isEmpty()
        );
    }

    private Member 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(MemberEntity 멤버_엔티티) {
        Long 저장된_멤버_ID = memberDao.save(멤버_엔티티);
        return memberRepository.findById(저장된_멤버_ID);
    }

    private Long 장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다(Member 멤버, Long 상품_ID) {
        var 응답 = 장바구니_상품_추가_요청(멤버, 상품_ID);
        return 헤더_ID_값_파싱(응답);
    }
}
