package cart.application;

import cart.dao.MemberCouponDao;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import cart.domain.coupon.StrategyFactory;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.dto.coupon.CouponRequest;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberCouponDao memberCouponDao;
    private final StrategyFactory strategyFactory;

    public CouponService(final CouponRepository couponRepository, final MemberCouponDao memberCouponDao, final StrategyFactory strategyFactory) {
        this.couponRepository = couponRepository;
        this.memberCouponDao = memberCouponDao;
        this.strategyFactory = strategyFactory;
    }

    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }

    public Long create(CouponRequest request) {
        return couponRepository.create(toCoupon(request));
    }

    public Coupon toCoupon(CouponRequest request) {
        return new Coupon(request.getName(), new Discount(strategyFactory.findStrategy(request.getType()), request.getAmount()));
    }


    public void delete(Long id) {
        couponRepository.delete(id);
    }

    public List<MemberCoupon> findUnUsedMemberCouponByMember(Member member) {
        List<MemberCouponEntity> entities = memberCouponDao.findByMemberId(member.getId());
        return entities.stream()
                .map(this::toMemberCoupon)
                .filter(MemberCoupon::isUnUsed)
                .collect(Collectors.toList());
    }

    private MemberCoupon toMemberCoupon(final MemberCouponEntity entity) {
        return new MemberCoupon(
                entity.getId(),
                new Coupon(
                        entity.getCoupon().getId(),
                        entity.getCoupon().getName(),
                        new Discount(
                                strategyFactory.findStrategy(entity.getCoupon().getDiscountType()),
                                entity.getCoupon().getAmount()
                        )
                ),
                entity.isUsed()
        );
    }

    public Long createMemberCoupons(Member member, Long couponId) {
        return memberCouponDao.create(member.getId(), couponId);
    }

    public List<MemberCoupon> findByIds(List<Long> ids) {
        List<MemberCouponEntity> entities = memberCouponDao.findByIds(ids);
        return entities.stream()
                .map(this::toMemberCoupon)
                .collect(Collectors.toList());
    }

    public List<MemberCoupon> findByMemberId(Long memberId) {
        List<MemberCouponEntity> entities = memberCouponDao.findByMemberId(memberId);
        return entities.stream()
                .map(this::toMemberCoupon)
                .collect(Collectors.toList());
    }

    public void updateCoupon(List<MemberCoupon> memberCoupons, Long memberId) {
        List<MemberCouponEntity> entities = memberCoupons.stream()
                .map(this::toMemberCouponEntity)
                .collect(Collectors.toList());
        memberCouponDao.updateCoupon(entities, memberId);
    }

    private MemberCouponEntity toMemberCouponEntity(final MemberCoupon memberCoupon) {
        return new MemberCouponEntity(
                memberCoupon.getId(),
                new CouponEntity(
                        memberCoupon.getCoupon().getId(),
                        memberCoupon.getCoupon().getName(),
                        memberCoupon.getCoupon().getDiscount().getStrategy().getStrategyName().name(),
                        memberCoupon.getCoupon().getDiscount().getAmount()
                ),
                memberCoupon.isUsed()
        );
    }
}
