package cart.acceptance.order;

import static cart.acceptance.cartitem.CartItemSteps.장바구니_상품_수량_변경_요청;
import static cart.acceptance.cartitem.CartItemSteps.장바구니_상품_전체_조회_요청;
import static cart.acceptance.cartitem.CartItemSteps.장바구니_상품_추가_요청;
import static cart.acceptance.common.CommonAcceptanceSteps.생성된_ID;
import static cart.acceptance.common.CommonAcceptanceSteps.응답을_검증한다;
import static cart.acceptance.common.CommonAcceptanceSteps.정상_생성;
import static cart.acceptance.order.OrderSteps.단일_주문_정보_조회_요청;
import static cart.acceptance.order.OrderSteps.상품_주문_요청;
import static cart.acceptance.order.OrderSteps.주문_상품_정보;
import static cart.acceptance.order.OrderSteps.주문_정보;
import static cart.acceptance.order.OrderSteps.주문_정보_검증;
import static cart.acceptance.order.OrderSteps.주문들_정보;
import static cart.acceptance.order.OrderSteps.주문들_정보_검증;
import static cart.acceptance.order.OrderSteps.회원의_주문_정보_조회_요청;
import static cart.acceptance.product.ProductSteps.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import cart.acceptance.AcceptanceTest;
import cart.member.domain.Member;
import cart.member.infrastructure.persistence.dao.MemberDao;
import cart.order.presentation.dto.OrderResponse;
import cart.order.presentation.dto.OrderResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Order 통합 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberDao memberDao;

    private Member 회원;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        memberDao.addMember(new Member(null, "email", "1234"));
        회원 = memberDao.getMemberById(1L);
    }

    @Test
    void 상품을_주문한다() {
        // given
        Long 말랑_ID = 생성된_ID(상품_생성_요청("말랑", 1000, "image"));
        Long 코코닥_ID = 생성된_ID(상품_생성_요청("코코닥", 2000, "image"));

        Long 장바구니_말랑_ID = 생성된_ID(장바구니_상품_추가_요청(회원, 말랑_ID));
        Long 장바구니_코코닥_ID = 생성된_ID(장바구니_상품_추가_요청(회원, 코코닥_ID));

        장바구니_상품_수량_변경_요청(회원, 장바구니_말랑_ID, 10);

        // when
        var 응답 = 상품_주문_요청(회원, 장바구니_말랑_ID, 장바구니_코코닥_ID);

        // then
        응답을_검증한다(응답, 정상_생성);
        assertThat(장바구니_상품_전체_조회_요청(회원)).isEmpty();
    }

    @Test
    void 단일_주문_정보를_조회한다() {
        // given
        Long 말랑_ID = 생성된_ID(상품_생성_요청("말랑", 1000, "image"));
        Long 코코닥_ID = 생성된_ID(상품_생성_요청("코코닥", 2000, "image"));

        Long 장바구니_말랑_ID = 생성된_ID(장바구니_상품_추가_요청(회원, 말랑_ID));
        Long 장바구니_코코닥_ID = 생성된_ID(장바구니_상품_추가_요청(회원, 코코닥_ID));

        장바구니_상품_수량_변경_요청(회원, 장바구니_말랑_ID, 10);

        Long 주문_ID = 생성된_ID(상품_주문_요청(회원, 장바구니_말랑_ID, 장바구니_코코닥_ID));

        // when
        OrderResponse 주문_정보 = 단일_주문_정보_조회_요청(회원, 주문_ID);

        // then
        OrderResponse 예상_주문_정보 = 주문_정보(주문_ID, 12000,
                주문_상품_정보(null, "말랑", 10000, 10, "image"),
                주문_상품_정보(null, "코코닥", 2000, 1, "image")
        );
        주문_정보_검증(주문_정보, 예상_주문_정보);
    }

    @Test
    void 회원의_모든_주문을_조회한다() {
        // given
        Long 말랑_ID = 생성된_ID(상품_생성_요청("말랑", 1000, "image"));
        Long 코코닥_ID = 생성된_ID(상품_생성_요청("코코닥", 2000, "image"));

        Long 장바구니_말랑_ID = 생성된_ID(장바구니_상품_추가_요청(회원, 말랑_ID));
        Long 장바구니_코코닥_ID = 생성된_ID(장바구니_상품_추가_요청(회원, 코코닥_ID));
        장바구니_상품_수량_변경_요청(회원, 장바구니_말랑_ID, 10);
        Long 주문_ID = 생성된_ID(상품_주문_요청(회원, 장바구니_말랑_ID, 장바구니_코코닥_ID));

        Long 장바구니_말랑_ID2 = 생성된_ID(장바구니_상품_추가_요청(회원, 말랑_ID));
        Long 주문2_ID = 생성된_ID(상품_주문_요청(회원, 장바구니_말랑_ID2));

        // when
        OrderResponses 주문들_정보 = 회원의_주문_정보_조회_요청(회원);

        // then
        OrderResponses 예상 = 주문들_정보(
                주문_정보(주문_ID, 12000,
                        주문_상품_정보(null, "말랑", 10000, 10, "image"),
                        주문_상품_정보(null, "코코닥", 2000, 1, "image")
                ),
                주문_정보(주문2_ID, 1000,
                        주문_상품_정보(null, "말랑", 1000, 1, "image")
                )
        );
        주문들_정보_검증(주문들_정보, 예상);
    }
}
