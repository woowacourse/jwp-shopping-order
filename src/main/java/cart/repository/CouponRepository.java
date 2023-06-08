package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.dto.CouponDto;
import cart.dao.dto.MemberCouponDto;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.repository.convertor.CouponConvertor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    private static final String COUPON_NOT_EXISTS_MESSAGE = "존재하지 않는 쿠폰입니다.";

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponRepository(final CouponDao couponDao,
                            final MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public MemberCoupon saveCoupon(Member member, Long couponId) {
        CouponDto couponDto = couponDao.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 쿠폰이 존재하지 않습니다."));

        return saveMemberCoupon(member, couponDto);
    }

    private MemberCoupon saveMemberCoupon(final Member member, final CouponDto couponDto) {
        MemberCouponDto memberCouponDto = new MemberCouponDto(member.getId(), couponDto.getId());
        Long id = memberCouponDao.insert(memberCouponDto);
        Coupon coupon = CouponConvertor.dtoToDomain(couponDto);

        return new MemberCoupon(id, member, coupon);
    }

    public List<MemberCoupon> findMemberCouponByMember(Member member) {
        List<MemberCouponDto> memberCouponDtos = memberCouponDao.findByMemberId(member.getId());
        return createMemberCoupons(member, memberCouponDtos);
    }

    private List<MemberCoupon> createMemberCoupons(Member member, final List<MemberCouponDto> memberCouponDtos) {
        List<MemberCoupon> memberCoupons = new ArrayList<>();

        for (MemberCouponDto memberCouponDto : memberCouponDtos) {
            Long couponId = memberCouponDto.getCouponId();
            CouponDto couponDto = couponDao.findById(couponId)
                    .orElseThrow(() -> new IllegalArgumentException(COUPON_NOT_EXISTS_MESSAGE));
            Coupon coupon = CouponConvertor.dtoToDomain(couponDto);
            memberCoupons.add(new MemberCoupon(memberCouponDto.getId(), member, coupon));
        }

        return memberCoupons;
    }

    public Optional<Coupon> findCouponByMemberAndMemberCouponId(final Member member, final Long memberCouponId) {
        MemberCouponDto memberCouponDto = findMemberCouponByMemberCouponId(memberCouponId, member.getId());
        CouponDto couponDto = findCouponByCouponId(memberCouponDto.getCouponId());
        return Optional.of(CouponConvertor.dtoToDomain(couponDto));
    }

    private MemberCouponDto findMemberCouponByMemberCouponId(final Long memberCouponId, final Long memberId) {
        return memberCouponDao.findByIdAndMemberId(memberCouponId, memberId)
                .orElseThrow(() -> new IllegalArgumentException(COUPON_NOT_EXISTS_MESSAGE));
    }

    private CouponDto findCouponByCouponId(final Long couponId) {
        return couponDao.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException(COUPON_NOT_EXISTS_MESSAGE));
    }

    public void deleteMemberCouponById(final Optional<Long> couponId) {
        couponId.ifPresent(this::deleteMemberCouponIfExists);
    }

    private void deleteMemberCouponIfExists(final Long memberCouponId) {
        int removeCount = memberCouponDao.deleteById(memberCouponId);

        if (removeCount == 0) {
            throw new IllegalArgumentException("쿠폰이 삭제되지 않았습니다.");
        }
    }

}
