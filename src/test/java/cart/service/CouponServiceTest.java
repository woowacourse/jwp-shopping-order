package cart.service;

import static cart.fixture.CouponFixture._3만원_이상_2천원_할인_쿠폰;
import static cart.fixture.CouponFixture._3만원_이상_배달비_3천원_할인_쿠폰;
import static cart.fixture.CouponFixture.쿠폰_발급;
import static cart.fixture.MemberFixture.사용자1;
import static cart.fixture.MemberFixture.사용자2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.dto.coupon.CouponResponse;
import cart.dto.coupon.CouponSaveRequest;
import cart.repository.CouponRepository;
import cart.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 사용자의_아이디를_입력받아_사용자가_사용하지_않은_쿠폰을_전체_조회한다() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Coupon coupon1 = couponRepository.save(쿠폰_발급(_3만원_이상_2천원_할인_쿠폰, member.getId()));
        final Coupon coupon2 = couponRepository.save(쿠폰_발급(_3만원_이상_배달비_3천원_할인_쿠폰, member.getId()));

        // when
        final List<CouponResponse> result = couponService.findAllByMemberId(member.getId());

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                new CouponResponse(coupon1.getId(), "30000원 이상 2000원 할인 쿠폰", "price", 2000L, 30000L),
                new CouponResponse(coupon2.getId(), "30000원 이상 배달비 할인 쿠폰", "delivery", 3000L, 30000L)
        ));
    }

    @Test
    void 쿠폰을_모든_사용자에게_발급한다() {
        // given
        final Member member1 = memberRepository.save(사용자1);
        final Member member2 = memberRepository.save(사용자2);
        final CouponSaveRequest request = new CouponSaveRequest("배달비 할인 쿠폰", "delivery", 3000L, 0L);

        // when
        couponService.issuance(request);

        // then
        assertAll(
                () -> assertThat(couponRepository.findAllByUsedAndMemberId(false, member1.getId())).hasSize(1),
                () -> assertThat(couponRepository.findAllByUsedAndMemberId(false, member2.getId())).hasSize(1)
        );
    }
}
