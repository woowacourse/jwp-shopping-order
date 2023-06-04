package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.dao.entity.CouponEntity;
import cart.dao.entity.MemberCouponEntity;
import cart.dao.entity.MemberEntity;
import cart.domain.Coupon;
import cart.domain.EmptyMemberCoupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.exception.CouponException;
import cart.exception.MemberCouponException;
import cart.exception.MemberException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class MemberCouponRepository {
    private final MemberCouponDao memberCouponDao;
    private final CouponDao couponDao;
    private final MemberDao memberDao;

    public MemberCouponRepository(final MemberCouponDao memberCouponDao, final CouponDao couponDao, final MemberDao memberDao) {
        this.memberCouponDao = memberCouponDao;
        this.couponDao = couponDao;
        this.memberDao = memberDao;
    }

    public Long save(final Coupon coupon, final LocalDateTime expiredAt, final Member member) {
        MemberCouponEntity memberCouponEntity = new MemberCouponEntity(
                null,
                member.getId(),
                coupon.getCouponInfo().getId(),
                false,
                expiredAt,
                null
        );
        return memberCouponDao.save(memberCouponEntity);
    }

    public List<MemberCoupon> findUsableByMember(final Member member) {
        Map<Long, Coupon> allCouponsById = couponDao.findAll().stream()
                .collect(Collectors.toMap(CouponEntity::getId, CouponEntity::toCoupon));

        return memberCouponDao.findUsableByMemberId(member.getId()).stream()
                .map(memberCouponEntity -> memberCouponEntity.toMemberCoupon(
                        allCouponsById.get(memberCouponEntity.getCouponId()),
                        member
                ))
                .collect(Collectors.toList());
    }

    public MemberCoupon findById(final Long id) {
        MemberCouponEntity memberCouponEntity = memberCouponDao.findById(id)
                .orElseThrow(() -> new MemberCouponException.NoExist("해당 멤버의 쿠폰이 존재하지 않습니다."));
        CouponEntity couponEntity = couponDao.findById(memberCouponEntity.getCouponId())
                .orElseThrow(() -> new CouponException.NoExist("해당 쿠폰이 존재하지 않습니다."));
        MemberEntity memberEntity = memberDao.findById(memberCouponEntity.getMemberId())
                .orElseThrow(() -> new MemberException.NoExist("해당 회원이 존재하지 않습니다."));
        return memberCouponEntity.toMemberCoupon(couponEntity.toCoupon(), memberEntity.toMember());
    }

    public void update(final MemberCoupon usedMemberCoupon) {
        if (usedMemberCoupon instanceof EmptyMemberCoupon) {
            return;
        }
        MemberCouponEntity memberCouponEntity = MemberCouponEntity.from(usedMemberCoupon);
        memberCouponDao.update(memberCouponEntity);
    }
}
