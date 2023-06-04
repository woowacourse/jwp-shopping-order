package cart.acceptance.steps;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.coupon.DiscountPolicyType;
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
}
