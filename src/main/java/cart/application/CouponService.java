package cart.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.dto.MemberCouponDto;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;

@Service
public class CouponService {

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;
    private final MemberService memberService;

    public CouponService(CouponDao couponDao, MemberCouponDao memberCouponDao, MemberService memberService) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
        this.memberService = memberService;
    }

    @Transactional(readOnly = true)
    public List<MemberCoupon> getMemberCouponsOf(Member member) {
        return memberCouponDao.selectAllBy(member.getId())
                .stream()
                .map(this::toMemberCoupon)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemberCoupon> getMemberCouponsBy(Member member, List<Long> ids) {
        return ids.stream()
                .map(id -> getMemberCouponBy(member, id))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MemberCoupon getMemberCouponBy(Member member, Long id) {
        MemberCoupon memberCoupon = toMemberCoupon(memberCouponDao.selectBy(id));
        memberCoupon.checkOwnerIs(member);
        return memberCoupon;
    }

    public Long giveCouponTo(Member member, Long couponId) {
        Coupon coupon = getCouponBy(couponId);
        MemberCoupon memberCoupon = new MemberCoupon(member, coupon);

        return memberCouponDao.insert(memberCoupon);
    }

    public Coupon getCouponBy(Long id) {
        return couponDao.selectBy(id);
    }

    public List<Coupon> getCoupons() {
        return couponDao.selectAll();
    }

    private MemberCoupon toMemberCoupon(MemberCouponDto dto) {
        return new MemberCoupon(
                dto.getId(),
                memberService.getMemberBy(dto.getOwnerId()),
                getCouponBy(dto.getId()),
                dto.getIsUsed()
        );
    }
}
