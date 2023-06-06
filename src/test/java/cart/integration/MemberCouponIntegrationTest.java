package cart.integration;

import cart.db.repository.JdbcCouponRepository;
import cart.db.repository.JdbcMemberCouponRepository;
import cart.db.repository.JdbcMemberRepository;
import cart.domain.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponType;
import cart.domain.coupon.MemberCoupon;
import cart.domain.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static cart.integration.IntegrationTestFixture.상태_코드를_검증한다;
import static cart.integration.MemberCouponIntegrationTestFixture.*;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberCouponIntegrationTest extends IntegrationTest {

    @Autowired
    private JdbcMemberRepository memberRepository;

    @Autowired
    private JdbcMemberCouponRepository memberCouponRepository;

    @Autowired
    private JdbcCouponRepository couponRepository;


    private Member 밀리;

    private Coupon 쿠폰_10퍼센트;
    private Coupon 쿠폰_1000원;

    private MemberCoupon 밀리_쿠폰_10퍼센트;
    private MemberCoupon 밀리_쿠폰_1000원;

    @BeforeEach
    void init() {
        밀리 = memberRepository.save(new Member("millie@email.com", "millie"));

        쿠폰_10퍼센트 = couponRepository.save(
                new Coupon("10퍼센트 할인 쿠폰", CouponType.RATE, BigDecimal.valueOf(10), new Money(1000)));
        쿠폰_1000원 = couponRepository.save(
                new Coupon("1000원 할인 쿠폰", CouponType.FIXED, BigDecimal.valueOf(1000), new Money(1000)));
        밀리_쿠폰_10퍼센트 = memberCouponRepository.save(new MemberCoupon(밀리, 쿠폰_10퍼센트, LocalDate.of(3000, 6, 8)));
        밀리_쿠폰_1000원 = memberCouponRepository.save(new MemberCoupon(밀리, 쿠폰_1000원, LocalDate.of(3000, 6, 8)));
    }

    @Nested
    class 쿠폰을_조회할_때 {

        @Test
        void 정상_조회한다() {
            var 응답 = 쿠폰_조회를_요청한다(밀리);

            상태_코드를_검증한다(응답, HttpStatus.OK);
            쿠폰_전체_조회_응답을_검증한다(응답, 쿠폰_전체_조회_정보를_반환한다(
                    List.of(정률_쿠폰_정보를_반환한다(밀리_쿠폰_10퍼센트.getId(), "10퍼센트 할인 쿠폰", 10, LocalDate.of(3000, 6, 8), 1000)),
                    List.of(정액_쿠폰_정보를_반환한다(밀리_쿠폰_1000원.getId(), "1000원 할인 쿠폰", 1000, LocalDate.of(3000, 6, 8), 1000))
            ));
        }
    }
}
