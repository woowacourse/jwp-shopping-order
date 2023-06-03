package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.repository.CouponRepository;
import cart.domain.repository.MemberRepository;
import cart.dto.response.ActiveCouponResponse;
import cart.dto.response.CouponDiscountResponse;
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
    @Autowired
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
        postCoupon(coupon1.getId());
        postCoupon(coupon2.getId());

        // when
        final ExtractableResponse<Response> response = getActiveCouponsResponse(totalProductAmount);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final List<ActiveCouponResponse> responses = response.jsonPath().getList(".", ActiveCouponResponse.class);
        final ActiveCouponResponse expected = new ActiveCouponResponse(coupon2.getId(), coupon2.getName(),
                coupon2.getMinAmount().getValue());
        assertThat(responses.size()).isEqualTo(1);
        assertThat(responses.get(0)).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("쿠폰 id와 총 상품 금액을 입력받아 할인 금액과 할인된 상품 금액을 조회할 수 있다.")
    @Test
    void showCouponDiscountAmount() {
        // given
        final Long couponId = coupon1.getId();
        final int totalProductAmount = 30000;

        // when
        final ExtractableResponse<Response> response = getDiscountAmountResponse(couponId, totalProductAmount);

        // then
        final CouponDiscountResponse couponDiscountResponse = response.as(CouponDiscountResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final int expectedDiscountAmount = coupon1.getDiscountAmount().getValue();
        assertThat(couponDiscountResponse.getDiscountAmount()).isEqualTo(expectedDiscountAmount);
        assertThat(couponDiscountResponse.getDiscountedProductAmount())
                .isEqualTo(totalProductAmount - expectedDiscountAmount);
    }

    @DisplayName("총 상품 금액이 쿠폰 할인 금액보다 작은 경우, 할인 금액과 할인된 상품 금액(0원)을 반환한다.")
    @Test
    void showCouponDiscountAmountWhenTotalAmountLessThanDiscountAmount() {
        // given
        final Long couponId = coupon2.getId();
        final int totalProductAmount = 10000;

        // when
        final ExtractableResponse<Response> response = getDiscountAmountResponse(couponId, totalProductAmount);

        // then
        final CouponDiscountResponse couponDiscountResponse = response.as(CouponDiscountResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final int expectedDiscountAmount = coupon2.getDiscountAmount().getValue();
        assertThat(couponDiscountResponse.getDiscountAmount()).isEqualTo(expectedDiscountAmount);
        assertThat(couponDiscountResponse.getDiscountedProductAmount())
                .isEqualTo(0);
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

    private ExtractableResponse<Response> getDiscountAmountResponse(final Long id, final int totalProductAmount) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when().get("/coupons/{id}/discount?total=" + totalProductAmount, id)
                .then().extract();
    }
}
