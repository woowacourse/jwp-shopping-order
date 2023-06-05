package cart.acceptance;

import static cart.acceptance.steps.CommonSteps.요청_결과의_상태를_검증한다;
import static cart.acceptance.steps.CommonSteps.정상_요청;
import static cart.acceptance.steps.CommonSteps.조회;
import static cart.acceptance.steps.CommonSteps.추가;
import static cart.acceptance.steps.CouponSteps.쿠폰_URL;
import static cart.acceptance.steps.CouponSteps.쿠폰_발급_URL;
import static cart.acceptance.steps.CouponSteps.쿠폰_발급_요청_정보;
import static cart.acceptance.steps.CouponSteps.쿠폰_전체_조회_결과를_확인한다;
import static cart.acceptance.steps.CouponSteps.쿠폰_정보;
import static cart.acceptance.steps.MemberSteps.사용자_저장;
import static cart.acceptance.steps.Request.Builder.사용자의_요청_생성;
import static cart.acceptance.steps.Request.Builder.요청_생성;
import static cart.domain.coupon.DiscountPolicyType.PRICE;
import static cart.fixture.MemberFixture.사용자1;
import static cart.fixture.MemberFixture.사용자2;

import cart.repository.MemberRepository;
import cart.test.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class CouponAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberRepository 사용자_저장소;

    @BeforeEach
    void init() {
        사용자_저장(사용자_저장소, 사용자1);
        사용자_저장(사용자_저장소, 사용자2);
    }

    @Test
    void 쿠폰을_모든_사용자에게_발급한다() {
        // when
        final var 쿠폰_발급_요청_결과 = 요청_생성()
                .전송_정보(쿠폰_발급_요청_정보("3만원 이상 3000원 할인 쿠폰", PRICE, 3000L, 30000L))
                .요청_위치(추가, 쿠폰_발급_URL)
                .요청_결과_반환();

        // then
        요청_결과의_상태를_검증한다(쿠폰_발급_요청_결과, 정상_요청);
    }

    @Test
    void 사용자의_미사용_쿠폰을_조회한다() {
        // given
        final var 쿠폰_발급_요청_정보 = 쿠폰_발급_요청_정보("3만원 이상 3000원 할인 쿠폰", PRICE, 3000L, 30000L);
        요청_생성().전송_정보(쿠폰_발급_요청_정보).요청_위치(추가, 쿠폰_발급_URL);

        // when
        final var 조회_요청_결과 = 사용자의_요청_생성(사용자1)
                .요청_위치(조회, 쿠폰_URL)
                .요청_결과_반환();

        // then
        요청_결과의_상태를_검증한다(조회_요청_결과, 정상_요청);
        쿠폰_전체_조회_결과를_확인한다(
                조회_요청_결과,
                쿠폰_정보("3만원 이상 3000원 할인 쿠폰", PRICE, 3000L, 30000L)
        );
    }
}
