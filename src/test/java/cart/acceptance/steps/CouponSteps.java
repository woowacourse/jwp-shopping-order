package cart.acceptance.steps;

import static cart.acceptance.steps.CommonSteps.조회;
import static cart.acceptance.steps.CommonSteps.추가;
import static cart.acceptance.steps.Request.Builder.사용자의_요청_생성;
import static cart.acceptance.steps.Request.Builder.요청_생성;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.coupon.DiscountPolicyType;
import cart.domain.member.Member;
import cart.dto.coupon.CouponResponse;
import cart.dto.coupon.CouponSaveRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class CouponSteps {

    public static final String 쿠폰_URL = "/coupons";
    public static final String 쿠폰_발급_URL = "/issuance";
    public static final String 쿠폰_없음 = null;

    public static CouponSaveRequest 쿠폰_발급_요청_정보(
            final String 쿠폰명,
            final DiscountPolicyType 타입,
            final Long 할인_가격,
            final Long 최소값
    ) {
        return new CouponSaveRequest(쿠폰명, 타입.name().toLowerCase(), 할인_가격, 최소값);
    }

    public static CouponResponse 쿠폰_정보(
            final String 쿠폰명,
            final DiscountPolicyType 타입,
            final Long 할인_가격,
            final Long 최소값
    ) {
        return new CouponResponse(null, 쿠폰명, 타입.name().toLowerCase(), 할인_가격, 최소값);
    }

    public static void 쿠폰_전체_조회_결과를_확인한다(
            final ExtractableResponse<Response> 요청_결과,
            final CouponResponse... 쿠폰_정보
    ) {
        final List<CouponResponse> 전체_상품_정보 = Arrays.stream(쿠폰_정보).collect(Collectors.toList());
        assertThat(요청_결과.jsonPath().getList(".", CouponResponse.class))
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(전체_상품_정보);
    }

    public static void 쿠폰을_발급한다(
            final String 쿠폰명,
            final DiscountPolicyType 타입,
            final Long 할인_가격,
            final Long 최소값
    ) {
        final CouponSaveRequest request = new CouponSaveRequest(쿠폰명, 타입.name().toLowerCase(), 할인_가격, 최소값);
        요청_생성()
                .전송_정보(request)
                .요청_위치(추가, 쿠폰_발급_URL);
    }

    public static String 사용자의_쿠폰_중_하나의_번호를_가져온다(final Member 사용자) {
        // when
        final var 조회_요청_결과 = 사용자의_요청_생성(사용자)
                .요청_위치(조회, 쿠폰_URL)
                .요청_결과_반환();

        final List<CouponResponse> result = 조회_요청_결과.jsonPath().getList(".", CouponResponse.class);
        return result.get(0).getId().toString();
    }
}
