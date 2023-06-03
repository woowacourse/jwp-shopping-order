package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.repository.CouponRepository;
import cart.domain.repository.MemberRepository;
import cart.dto.response.CouponResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class CouponIntegrationTest extends IntegrationTest {

    @Autowired
    private MemberRepository memberRepository;
    private CouponRepository couponRepository;

    private Member member;
    private Coupon coupon1;
    private Coupon coupon2;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberRepository.findById(1L);
        final List<Coupon> coupons = couponRepository.findAll();
        coupon1 = coupons.get(0);
        coupon2 = coupons.get(1);
    }

    @DisplayName("전체 쿠폰 목록을 사용자에 맞게 조회할 수 있다.")
    @Test
    void showCouponByMember() {
        // given
        // when
        final ExtractableResponse<Response> response = getCouponsResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("사용자에게 쿠폰을 발급할 수 있다.")
    @Test
    void publishCouponToMember() {
        // given
        final Long couponId = coupon1.getId();

        // when
        final ExtractableResponse<Response> response = postCoupon(couponId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        final List<CouponResponse> couponResponses = getCouponsResponse().jsonPath().getList(".", CouponResponse.class);
        final CouponResponse couponResponse = couponResponses.stream()
                .filter(it -> it.getCouponId().equals(couponId))
                .findAny().orElseThrow(NoSuchElementException::new);
        assertThat(couponResponse.getIsPublished()).isTrue();
    }

    @DisplayName("현재 총 상품 금액으로 사용자가 사용 가능한 쿠폰들을 조회할 수 있다.")
    @Test
    void getActiveCouponsByTotalProductAmount() {
        // given
        final int totalProductAmount = 1000;

        // when
        final ExtractableResponse<Response> response = getActiveCouponsResponse(totalProductAmount);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final List<CouponResponse> couponResponses = response.jsonPath().getList(".", CouponResponse.class);
        final CouponResponse expected = new CouponResponse(coupon1.getId(), coupon1.getName(),
                coupon1.getMinAmount().getValue(), coupon1.getDiscountAmount().getValue(), false);
        assertThat(couponResponses.size()).isEqualTo(1);
        assertThat(couponResponses).usingRecursiveComparison()
                .ignoringFields("isPublished")
                .isEqualTo(expected);

    }

    private ExtractableResponse<Response> getCouponsResponse() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/coupons")
                .then().extract();
    }

    private ExtractableResponse<Response> postCoupon(final Long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().post("/coupons/{id}", id)
                .then().extract();
    }

    private ExtractableResponse<Response> getActiveCouponsResponse(final int totalProductAmount) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/coupons/active?total=" + totalProductAmount)
                .then().extract();
    }
}
