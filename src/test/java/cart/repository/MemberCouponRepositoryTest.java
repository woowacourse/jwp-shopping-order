package cart.repository;

import static cart.fixture.CouponFixture._3만원_이상_2천원_할인_쿠폰;
import static cart.fixture.CouponFixture._3만원_이상_배달비_3천원_할인_쿠폰;
import static cart.fixture.MemberFixture.사용자1;
import static cart.fixture.MemberFixture.사용자2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.cart.MemberCoupon;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class MemberCouponRepositoryTest {

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void 모든_사용자_쿠폰을_저장한다() {
        // given
        final Coupon coupon = couponRepository.save(_3만원_이상_2천원_할인_쿠폰);
        final Member member1 = memberRepository.save(사용자1);
        final Member member2 = memberRepository.save(사용자2);

        // when
        memberCouponRepository.saveAll(List.of(
                new MemberCoupon(member1.getId(), coupon),
                new MemberCoupon(member2.getId(), coupon)
        ));

        // then
        assertAll(
                () -> assertThat(couponRepository.findAllByMemberId(member1.getId())).hasSize(1),
                () -> assertThat(couponRepository.findAllByMemberId(member2.getId())).hasSize(1)
        );
    }

    @Test
    void 사용자_아이디를_입력받아_전체_쿠폰을_조회한다() {
        // given
        final Coupon coupon1 = couponRepository.save(_3만원_이상_2천원_할인_쿠폰);
        final Coupon coupon2 = couponRepository.save(_3만원_이상_배달비_3천원_할인_쿠폰);
        final Member member = memberRepository.save(사용자1);
        memberCouponRepository.saveAll(List.of(
                new MemberCoupon(member.getId(), coupon1),
                new MemberCoupon(member.getId(), coupon2)
        ));

        // when
        final List<MemberCoupon> result = memberCouponRepository.findAllByMemberId(member.getId());

        // then
        assertThat(result).hasSize(2);
    }
}
