package cart.integration;


import static cart.fixture.MemberFixture.MEMBER;
import static cart.integration.MemberUtils.회원_추가;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.Member;
import cart.dto.CouponResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CouponController 통합테스트 은(는)")
public class CouponIntegrationTest extends IntegrationTest {

    private static final String API_URL = "/coupons";
    private static final Coupon 천원_할인_쿠폰 = new Coupon(1L, "1000원 할인 쿠폰", CouponType.FIXED, 1000);
    private static final Coupon 십프로_할인_쿠폰 = new Coupon(2L, "10% 할인 쿠폰", CouponType.RATE, 10);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 특정한_회원의_모든_쿠폰을_조회한다() {
        // given
        회원_추가(MEMBER, jdbcTemplate);
        쿠폰_추가(천원_할인_쿠폰);
        쿠폰_추가(십프로_할인_쿠폰);
        회원에게_쿠폰_발급(MEMBER, 천원_할인_쿠폰);
        회원에게_쿠폰_발급(MEMBER, 십프로_할인_쿠폰);

        // when
        ExtractableResponse<Response> response = 회원의_쿠폰을_조회한다(MEMBER);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<CouponResponse> body = response.as(new ParameterizedTypeReference<List<CouponResponse>>() {
                }.getType()
        );
        쿠폰을_검증한다(body.get(0), 천원_할인_쿠폰);
        쿠폰을_검증한다(body.get(1), 십프로_할인_쿠폰);
    }

    private static ExtractableResponse<Response> 회원의_쿠폰을_조회한다(Member 회원) {
        return given().log().all()
                .auth().preemptive().basic(회원.getEmail(), 회원.getPassword())
                .when()
                .get(API_URL)
                .then()
                .log().all()
                .extract();
    }

    private void 쿠폰을_검증한다(CouponResponse 결과, Coupon 쿠폰) {
        assertAll(
                () -> assertThat(결과.getId()).isEqualTo(쿠폰.getId()),
                () -> assertThat(결과.getName()).isEqualTo(쿠폰.getName()),
                () -> assertThat(결과.getType()).isEqualTo(쿠폰.getType().name()),
                () -> assertThat(결과.getDiscountAmount()).isEqualTo(쿠폰.getDiscountAmount())
        );
    }

    private void 쿠폰_추가(Coupon coupon) {
        String sql = "INSERT INTO coupon (id, name, type, discount_amount) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, coupon.getId(), coupon.getName(), coupon.getType().name(), coupon.getDiscountAmount());
    }

    private void 회원에게_쿠폰_발급(Member member, Coupon coupon) {
        String sql = "INSERT INTO member_coupon(member_id, coupon_id) VALUES (?,?)";
        jdbcTemplate.update(sql, member.getId(), coupon.getId());
    }
}
