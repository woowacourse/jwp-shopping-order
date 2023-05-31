package cart.integration;

import static cart.integration.steps.CartItemStep.장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다;
import static cart.integration.steps.OrderStep.주문_추가_요청;
import static cart.integration.steps.ProductStep.상품_생성_요청_생성;
import static cart.integration.steps.ProductStep.상품_생성_요청후_상품_ID를_리턴한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import cart.exception.ErrorMessage;
import cart.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class OrderIntegrationTest extends IntegrationTest {
    private MemberEntity 멤버_엔티티;
    private MemberEntity 다른_멤버_엔티티;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        멤버_엔티티 = new MemberEntity
                (null, "vero@email", "asdf1234", 20000, null, null);
        다른_멤버_엔티티 = new MemberEntity
                (null, "otheruser@email", "asdf1234", 3000, null, null);
    }

    @Test
    void 주문을_저장한다() throws Exception {
        // given
        Member 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        Long 치킨_ID = 상품_생성_요청후_상품_ID를_리턴한다(상품_생성_요청_생성("치킨", 10_000, "http://example.com/chicken.jpg"));
        Long 첫번째_장바구니_상품_ID = 장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다(멤버, 치킨_ID);
        Long 피자_ID = 상품_생성_요청후_상품_ID를_리턴한다(상품_생성_요청_생성("피자", 30_000, "http://example.com/pizza.jpg"));
        Long 두번째_장바구니_상품_ID = 장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다(멤버, 피자_ID);

        // when
        var 응답 = 주문_추가_요청(멤버, List.of(첫번째_장바구니_상품_ID, 두번째_장바구니_상품_ID), 1000, objectMapper);
        Member 조회한_멤버 = memberRepository.findById(멤버.getId());

        // then
        assertAll(
                () -> assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(조회한_멤버.getPoint()).isEqualTo(멤버.getPoint() + 3000)
        );
    }

    private Member 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(MemberEntity 멤버_엔티티) {
        Long 저장된_멤버_ID = memberDao.save(멤버_엔티티);
        return memberRepository.findById(저장된_멤버_ID);
    }

    @Test
    void 멤버가_갖는_포인트보다_많이_사용하면_예외를_응답한다() throws Exception {
        // given
        Member 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        Long 치킨_ID = 상품_생성_요청후_상품_ID를_리턴한다(상품_생성_요청_생성("치킨", 10_000, "http://example.com/chicken.jpg"));
        Long 첫번째_장바구니_상품_ID = 장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다(멤버, 치킨_ID);

        // when
        var 응답 = 주문_추가_요청(멤버, List.of(첫번째_장바구니_상품_ID), 30000, objectMapper);

        // then
        assertAll(
                () -> assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(응답.jsonPath().getString("message"))
                        .isEqualTo(ErrorMessage.INVALID_MEMBER_POINT_LESS_THAN_USED_POINT.getMessage())
        );
    }

    @Test
    void 상품_가격보다_많은_포인트를_사용하면_예외를_응답한다() throws Exception {
        // given
        Member 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        Long 치킨_ID = 상품_생성_요청후_상품_ID를_리턴한다(상품_생성_요청_생성("치킨", 1000, "http://example.com/chicken.jpg"));
        Long 첫번째_장바구니_상품_ID = 장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다(멤버, 치킨_ID);

        // when
        var 응답 = 주문_추가_요청(멤버, List.of(첫번째_장바구니_상품_ID), 10000, objectMapper);

        // then
        assertAll(
                () -> assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(응답.jsonPath().getString("message"))
                        .isEqualTo(ErrorMessage.INVALID_POINT_MORE_THAN_PRICE.getMessage())
        );
    }

    @Test
    void 멤버의_장바구니_상품이_아닌_경우_예외를_응답한다() throws Exception {
        // given
        Member 멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(멤버_엔티티);
        Member 다른_멤버 = 멤버를_저장하고_ID를_갖는_멤버를_리턴한다(다른_멤버_엔티티);
        Long 치킨_ID = 상품_생성_요청후_상품_ID를_리턴한다(상품_생성_요청_생성("치킨", 10_000, "http://example.com/chicken.jpg"));
        Long 첫번째_장바구니_상품_ID = 장바구니_상품_추가_후_장바구니_상품_ID를_리턴한다(멤버, 치킨_ID);

        // when
        var 응답 = 주문_추가_요청(다른_멤버, List.of(첫번째_장바구니_상품_ID), 10000, objectMapper);

        // then
        assertAll(
                () -> assertThat(응답.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value()),
                () -> assertThat(응답.jsonPath().getString("message"))
                        .isEqualTo(ErrorMessage.INVALID_CART_ITEM_OWNER.getMessage())
        );
    }
}
