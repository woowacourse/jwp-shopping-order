package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.domain.coupon.Coupon;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.entity.MemberEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponRepository {

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;
    private final MemberDao memberDao;

    public MemberCouponRepository(CouponDao couponDao, MemberCouponDao memberCouponDao, MemberDao memberDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
        this.memberDao = memberDao;
    }

    public Optional<MemberCoupon> findById(Long id) {
        Optional<MemberCouponEntity> savedMemberCoupon = memberCouponDao.findById(id);
        if (savedMemberCoupon.isEmpty()) {
            return Optional.empty();
        }
        MemberCouponEntity memberCoupon = savedMemberCoupon.get();
        Coupon coupon = toCoupon(memberCoupon.getCouponId());
        Member member = toMember(memberCoupon.getMemberId());
        return Optional.of(new MemberCoupon(id, member, coupon, memberCoupon.getExpiredDate()));
    }

    private Coupon toCoupon(Long couponId) {
        return couponDao.findById(couponId)
                .map(CouponEntity::toDomain)
                .orElseThrow();
    }

    private Member toMember(Long memberId) {
        return memberDao.findById(memberId)
                .map(MemberEntity::toDomain)
                .orElseThrow();
    }

    public MemberCoupon save(MemberCoupon memberCoupon) {
        MemberCouponEntity memberCouponEntity = toEntity(memberCoupon);
        Long id = memberCouponDao.save(memberCouponEntity);
        return new MemberCoupon(id, memberCoupon.getMember(), memberCoupon.getCoupon(), memberCoupon.getExpiredDate());
    }

    private MemberCouponEntity toEntity(MemberCoupon memberCoupon) {
        return new MemberCouponEntity(
                memberCoupon.getMember().getId(),
                memberCoupon.getCoupon().getId(),
                memberCoupon.getExpiredDate()
        );
    }

    public void delete(MemberCoupon memberCoupon) {
        memberCouponDao.deleteById(memberCoupon.getId());
    }
}
