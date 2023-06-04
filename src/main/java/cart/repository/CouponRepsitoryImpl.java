package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponTypes;
import cart.domain.coupon.DiscountType;
import cart.domain.repository.CouponRepository;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.exception.CouponException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponRepsitoryImpl implements CouponRepository {
    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponRepsitoryImpl(CouponDao couponDao, MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    @Override
    public Long save(Member member, Long couponId) {
        if (!couponDao.checkById(couponId)) {
            throw new CouponException("해당 쿠폰을 찾을 수 없습니다.");
        }

        if (memberCouponDao.checkByCouponIdAndMemberId(member.getId(), couponId)) {
            throw new CouponException("이미 존재하는 쿠폰입니다.");
        }

        return memberCouponDao.save(new MemberCouponEntity(couponId, member.getId(), true));
    }

    @Override
    public List<Coupon> findByMemberId(Member member) {
        return memberCouponDao.findAllByMemberId(member.getId()).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Coupon> findAll() {
        return couponDao.findAll().stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    private Coupon toDomain(CouponEntity entity) {
        CouponTypes couponType = DiscountType.from(entity.getDiscountType());
        return new Coupon(entity.getId(), entity.getName(), couponType,
                entity.getMinimumPrice(), entity.getDiscountPrice(), entity.getDiscountRate());
    }
}
