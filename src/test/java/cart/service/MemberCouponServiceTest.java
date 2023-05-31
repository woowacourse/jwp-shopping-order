package cart.service;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.policy.PriceDiscountPolicy;
import cart.dto.MemberCouponDto;
import cart.entity.MemberCouponEntity;
import cart.repository.CouponRepository;
import cart.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class MemberCouponServiceTest {

    @Autowired
    private MemberCouponService memberCouponService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcInsert;

    @BeforeEach
    void init() {
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_coupon")
                .usingColumns("coupon_id", "member_id", "used")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    void 해당멤버의_쿠폰을_조회한다() {
        // given when
        final Member savedMember = memberRepository.save(new Member("wuga@naver.com", "1234"));
        final Coupon savedCoupon = couponRepository.saveCoupon(
                new Coupon("1000원이상 결제시 1원쿠폰", new PriceDiscountPolicy(1L), 1000L));

        final MemberCouponEntity memberCouponEntity = new MemberCouponEntity(savedCoupon.getId(), savedMember.getId(),
                false);
        final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(memberCouponEntity);
        final long savedMemberCouponId = jdbcInsert.executeAndReturnKey(parameterSource).longValue();

        // then
        final List<MemberCouponDto> result = memberCouponService.findByMemberId(savedMember.getId());

        assertThat(result).hasSize(1);
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(
                        List.of(new MemberCouponDto(
                                savedMemberCouponId,
                                savedCoupon.getName(),
                                savedCoupon.getType(),
                                savedCoupon.getDiscountPolicy().getDiscountPrice(),
                                savedCoupon.getMinimumPrice()
                        )));
    }
}
