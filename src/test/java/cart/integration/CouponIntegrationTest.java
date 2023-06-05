package cart.integration;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.member.Member;
import cart.domain.member.MemberRepository;
import cart.domain.memberCoupon.MemberCoupon;
import cart.domain.memberCoupon.MemberCouponRepository;
import cart.dto.IssuableSearchCouponResponse;
import cart.dto.MemberCouponRequest;
import cart.dto.MemberCouponResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class CouponIntegrationTest extends IntegrationTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private CouponRepository couponRepository;

    private Member member;
    private Coupon coupon;
    private Coupon coupon2;

    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberRepository.findById(1L);
        coupon = couponRepository.findById(1L);
        coupon2 = couponRepository.findById(2L);
    }

    @Test
    void 멤버에게_쿠폰을_발급한다() {
        MemberCouponRequest memberCouponRequest = new MemberCouponRequest(1L);

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .body(memberCouponRequest)
                .when()
                .post("/users/coupons")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 멤버의_모든_쿠폰을_조회한다() {
        initMemberCoupons(member);
        memberCouponRepository.add(new MemberCoupon(member, coupon));

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/users/coupons")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> resultMemberCouponIds = response.jsonPath().getList(".", MemberCouponResponse.class).stream()
                .map(MemberCouponResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultMemberCouponIds).containsAll(Arrays.asList(coupon.getId()));
    }

    @Test
    void 발급_가능_여부_정보가_포함된_모든_쿠폰들을_조회한다() {
        initMemberCoupons(member);
        memberCouponRepository.add(new MemberCoupon(member, coupon));

        ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().basic(member.getEmail(), member.getPassword())
                .when()
                .get("/coupons")
                .then()
                .log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<IssuableSearchCouponResponse> issuableSearchCouponResponses = response.jsonPath().getList(".", IssuableSearchCouponResponse.class);
        assertThat(issuableSearchCouponResponses.get(0)).usingRecursiveComparison()
                .isEqualTo(IssuableSearchCouponResponse.of(coupon2, true));
        assertThat(issuableSearchCouponResponses.get(1)).usingRecursiveComparison()
                .isEqualTo(IssuableSearchCouponResponse.of(coupon, false));
    }

    private void initMemberCoupons(Member member) {
        List<MemberCoupon> memberCoupons = memberCouponRepository.findMemberCouponsByMemberId(member.getId());
        memberCoupons.forEach(memberCoupon -> memberCouponRepository.delete(memberCoupon.getId()));
    }
}
