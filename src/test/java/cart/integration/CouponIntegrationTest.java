package cart.integration;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
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

    private Member member;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        member = memberRepository.findById(1L);
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
        final Long couponId = 1L;

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
                .when().post("/coupons/" + id)
                .then().extract();
    }
}
