package cart.acceptance;

import static cart.acceptance.steps.CartItemSteps.단일_장바구니_URL;
import static cart.acceptance.steps.CartItemSteps.장바구니_수량_변경_요청_정보;
import static cart.acceptance.steps.CartItemSteps.장바구니에_상품을_추가하고_번호를_반환한다;
import static cart.acceptance.steps.CommonSteps.수정;
import static cart.acceptance.steps.CommonSteps.요청_결과가_반환하는_리소스의_위치가_존재하는지_확인한다;
import static cart.acceptance.steps.CommonSteps.요청_결과의_상태를_검증한다;
import static cart.acceptance.steps.CommonSteps.정상_생성;
import static cart.acceptance.steps.CommonSteps.정상_요청;
import static cart.acceptance.steps.CommonSteps.조회;
import static cart.acceptance.steps.CommonSteps.추가;
import static cart.acceptance.steps.CouponSteps.사용자의_쿠폰_중_하나의_번호를_가져온다;
import static cart.acceptance.steps.CouponSteps.쿠폰_없음;
import static cart.acceptance.steps.CouponSteps.쿠폰을_발급한다;
import static cart.acceptance.steps.MemberSteps.사용자_저장;
import static cart.acceptance.steps.OrderSteps.단일_주문_URL;
import static cart.acceptance.steps.OrderSteps.전체_주문_정보를_확인한다;
import static cart.acceptance.steps.OrderSteps.주문_URL;
import static cart.acceptance.steps.OrderSteps.주문_번호를_구한다;
import static cart.acceptance.steps.OrderSteps.주문_상품_정보;
import static cart.acceptance.steps.OrderSteps.주문_요청_정보;
import static cart.acceptance.steps.OrderSteps.주문_정보;
import static cart.acceptance.steps.OrderSteps.주문_정보를_확인한다;
import static cart.acceptance.steps.ProductSteps.상품_번호를_구한다;
import static cart.acceptance.steps.ProductSteps.상품_추가_요청;
import static cart.acceptance.steps.Request.Builder.사용자의_요청_생성;
import static cart.domain.coupon.DiscountPolicyType.PRICE;
import static cart.fixture.MemberFixture.사용자1;
import static cart.fixture.MemberFixture.사용자2;
import static cart.fixture.ProductFixture.상품_28900원;
import static cart.fixture.ProductFixture.상품_8900원;

import cart.repository.MemberRepository;
import cart.test.AcceptanceTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class OrderAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository 사용자_저장소;

    private String 장바구니1_상품_번호;
    private String 장바구니2_상품_번호;

    @BeforeEach
    void init() {
        사용자_저장(사용자_저장소, 사용자1);
        사용자_저장(사용자_저장소, 사용자2);
        final var 상품_8900원_상품_번호 = 상품_번호를_구한다(상품_추가_요청(상품_8900원));
        final var 상품_28900원_상품_번호 = 상품_번호를_구한다(상품_추가_요청(상품_28900원));
        장바구니1_상품_번호 = 장바구니에_상품을_추가하고_번호를_반환한다(사용자1, 상품_8900원_상품_번호);
        장바구니2_상품_번호 = 장바구니에_상품을_추가하고_번호를_반환한다(사용자1, 상품_28900원_상품_번호);
        쿠폰을_발급한다("3만원 이상 3000원 할인 쿠폰", PRICE, 3000L, 30000L);
    }

    @Test
    void 상품을_주문한다() {
        // given
        final var 쿠폰_번호 = 사용자의_쿠폰_중_하나의_번호를_가져온다(사용자1);
        final var 주문_요청_정보 = 주문_요청_정보(List.of(장바구니1_상품_번호, 장바구니2_상품_번호), 쿠폰_번호);

        // when
        final var 주문_요청_결과 = 사용자의_요청_생성(사용자1)
                .전송_정보(주문_요청_정보)
                .요청_위치(추가, 주문_URL)
                .요청_결과_반환();

        // then
        요청_결과의_상태를_검증한다(주문_요청_결과, 정상_생성);
        요청_결과가_반환하는_리소스의_위치가_존재하는지_확인한다(주문_요청_결과);
    }

    @Test
    void 주문을_전체_조회한다() {
        // given
        final var 주문_요청_정보1 = 주문_요청_정보(List.of(장바구니1_상품_번호), 쿠폰_없음);
        사용자의_요청_생성(사용자1).전송_정보(주문_요청_정보1).요청_위치(추가, 주문_URL).요청_결과_반환();

        final var 쿠폰_번호 = 사용자의_쿠폰_중_하나의_번호를_가져온다(사용자1);
        사용자의_요청_생성(사용자1).전송_정보(장바구니_수량_변경_요청_정보(2)).요청_위치(수정, 단일_장바구니_URL(장바구니2_상품_번호)).요청_결과_반환();
        final var 주문_요청_정보2 = 주문_요청_정보(List.of(장바구니2_상품_번호), 쿠폰_번호);
        사용자의_요청_생성(사용자1).전송_정보(주문_요청_정보2).요청_위치(추가, 주문_URL).요청_결과_반환();

        // when
        final var 전체_조회_요청_결과 = 사용자의_요청_생성(사용자1)
                .요청_위치(조회, 주문_URL)
                .요청_결과_반환();

        // then
        요청_결과의_상태를_검증한다(전체_조회_요청_결과, 정상_요청);
        전체_주문_정보를_확인한다(전체_조회_요청_결과,
                주문_정보(
                        8900L,
                        0L,
                        3000L,
                        주문_상품_정보(상품_8900원, 1)
                ),
                주문_정보(
                        57800L,
                        3000L,
                        3000L,
                        주문_상품_정보(상품_28900원, 2)
                )
        );
    }

    @Test
    void 주문을_단일_조회한다() {
        // given
        final var 쿠폰_번호 = 사용자의_쿠폰_중_하나의_번호를_가져온다(사용자1);
        final var 주문_요청_정보 = 주문_요청_정보(List.of(장바구니1_상품_번호, 장바구니2_상품_번호), 쿠폰_번호);
        final var 주문_요청 = 사용자의_요청_생성(사용자1).전송_정보(주문_요청_정보).요청_위치(추가, 주문_URL).요청_결과_반환();
        final var 주문_번호 = 주문_번호를_구한다(주문_요청);

        // when
        final var 조회_요청_결과 = 사용자의_요청_생성(사용자1)
                .요청_위치(조회, 단일_주문_URL(주문_번호))
                .요청_결과_반환();

        // then
        요청_결과의_상태를_검증한다(조회_요청_결과, 정상_요청);
        주문_정보를_확인한다(조회_요청_결과, 주문_정보(
                37800L,
                3000L,
                3000L,
                주문_상품_정보(상품_8900원, 1),
                주문_상품_정보(상품_28900원, 1)
        ));
    }
}
