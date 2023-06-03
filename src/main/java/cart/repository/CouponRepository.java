package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.dto.CouponDto;
import cart.dto.MemberCouponDto;
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
    private final MemberDao memberDao;

    public CouponRepository(final CouponDao couponDao,
                            final MemberCouponDao memberCouponDao,
                            final MemberDao memberDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
        this.memberDao = memberDao;
    }

    public List<MemberCoupon> saveCoupon(Member member) {
        Member memberByEmail = memberDao.getMemberByEmail(member.getEmail());
        List<CouponDto> couponDtos = couponDao.findAll();
        return saveMemberCoupons(memberByEmail, couponDtos);
    }

    private List<MemberCoupon> saveMemberCoupons(final Member memberByEmail, final List<CouponDto> couponDtos) {
        List<MemberCoupon> memberCoupons = new ArrayList<>();

        for (CouponDto couponDto : couponDtos) {
            MemberCouponDto memberCouponDto = new MemberCouponDto(memberByEmail.getId(), couponDto.getId());
            Long id = memberCouponDao.insert(memberCouponDto);
            Coupon coupon = CouponConvertor.dtoToDomain(couponDto);
            memberCoupons.add(new MemberCoupon(id, memberByEmail, coupon));
        }

        return memberCoupons;
    }

    public List<MemberCoupon> findMemberCouponByMember(Member member) {
        Member memberByEmail = memberDao.getMemberByEmail(member.getEmail());
        List<MemberCouponDto> memberCouponDtos = memberCouponDao.findByMemberId(memberByEmail.getId());
        return getMemberCoupons(memberByEmail, memberCouponDtos);
    }

    private List<MemberCoupon> getMemberCoupons(Member member, final List<MemberCouponDto> memberCouponDtos) {
        List<MemberCoupon> memberCoupons = new ArrayList<>();

        for (MemberCouponDto memberCouponDto : memberCouponDtos) {
            Long couponId = memberCouponDto.getCouponId();
            CouponDto couponDto = couponDao.findById(couponId).orElseThrow(() -> new IllegalArgumentException(COUPON_NOT_EXISTS_MESSAGE));
            Coupon coupon = CouponConvertor.dtoToDomain(couponDto);
            memberCoupons.add(new MemberCoupon(memberCouponDto.getId(), member, coupon));
        }

        return memberCoupons;
    }

    public Optional<Coupon> findCouponByMemberAndMemberCouponId(final Member member, final Long memberCouponId) {
        Member memberByEmail = memberDao.getMemberByEmail(member.getEmail());
        MemberCouponDto memberCouponDto = getMemberCouponByMemberCouponId(memberCouponId, memberByEmail.getId());
        CouponDto couponDto = getCouponByCouponId(memberCouponDto.getCouponId());
        Coupon coupon = new Coupon(couponDto.getId(), couponDto.getName(), couponDto.getDiscountRate(), couponDto.getDiscountPrice());
        return Optional.of(coupon);
    }

    private MemberCouponDto getMemberCouponByMemberCouponId(final Long memberCouponId, final Long memberId) {
        return memberCouponDao.findByIdAndMemberId(memberCouponId, memberId)
                .orElseThrow(() -> new IllegalArgumentException(COUPON_NOT_EXISTS_MESSAGE));
    }

    private CouponDto getCouponByCouponId(final Long couponId) {
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
