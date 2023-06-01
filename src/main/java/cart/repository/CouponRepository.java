package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.dto.CouponDto;
import cart.dto.MemberCouponDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

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
            Coupon coupon = couponDtoToCoupon(couponDto);
            memberCoupons.add(new MemberCoupon(id, coupon));
        }

        return memberCoupons;
    }

    private Coupon couponDtoToCoupon(final CouponDto couponDto) {
        return new Coupon(
                couponDto.getId(),
                couponDto.getName(),
                couponDto.getDiscountRate(),
                couponDto.getDiscountPrice()
        );
    }

    public List<MemberCoupon> findMemberCouponByMember(Member member) {
        Member memberByEmail = memberDao.getMemberByEmail(member.getEmail());
        List<MemberCouponDto> memberCouponDtos = memberCouponDao.findByMemberId(memberByEmail.getId());
        return getMemberCoupons(memberCouponDtos);
    }

    private List<MemberCoupon> getMemberCoupons(final List<MemberCouponDto> memberCouponDtos) {
        List<MemberCoupon> memberCoupons = new ArrayList<>();

        for (MemberCouponDto memberCouponDto : memberCouponDtos) {
            Long couponId = memberCouponDto.getCouponId();
            CouponDto couponDto = couponDao.findById(couponId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
            Coupon coupon = couponDtoToCoupon(couponDto);
            memberCoupons.add(new MemberCoupon(memberCouponDto.getId(), coupon));
        }

        return memberCoupons;
    }

    public Optional<Coupon> findCouponByMemberCouponId(final Long memberCouponId) {
        MemberCouponDto memberCouponDto = getMemberCouponByMemberCouponId(memberCouponId);
        CouponDto couponDto = getCouponByCouponId(memberCouponDto.getCouponId());
        Coupon coupon = new Coupon(couponDto.getId(), couponDto.getName(), couponDto.getDiscountRate(), couponDto.getDiscountPrice());
        return Optional.of(coupon);
    }

    private MemberCouponDto getMemberCouponByMemberCouponId(final Long memberCouponId) {
        return memberCouponDao.findById(memberCouponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
    }

    private CouponDto getCouponByCouponId(final Long couponId) {
        return couponDao.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
    }

}
