package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.domain.member.MemberCoupon;
import cart.entity.MemberCouponEntity;
import cart.exception.CouponNotFoundException;
import cart.exception.MemberNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MemberCouponRepository {

    private final MemberCouponDao memberCouponDao;
    private final MemberDao memberDao;
    private final CouponDao couponDao;

    public MemberCouponRepository(final MemberCouponDao memberCouponDao, final MemberDao memberDao, final CouponDao couponDao) {
        this.memberCouponDao = memberCouponDao;
        this.memberDao = memberDao;
        this.couponDao = couponDao;
    }

    public MemberCoupon save(final MemberCoupon memberCoupon) {
        if (Objects.isNull(memberCoupon.getId())) {
            final MemberCouponEntity entity = memberCouponDao.insert(MemberCouponEntity.from(memberCoupon));
            return new MemberCoupon(entity.getId(), memberCoupon.getMember(), memberCoupon.getCoupon(), memberCoupon.isUsed());
        }
        memberCouponDao.update(MemberCouponEntity.from(memberCoupon));
        return memberCoupon;
    }

    public Optional<MemberCoupon> findById(final Long id) {
        return memberCouponDao.findById(id)
                .map(this::getMemberCoupon);
    }

    private MemberCoupon getMemberCoupon(final MemberCouponEntity entity) {
        return new MemberCoupon(
                entity.getId(),
                memberDao.findById(entity.getMemberId())
                        .orElseThrow(MemberNotFoundException::new).toDomain(),
                couponDao.findById(entity.getCouponId())
                        .orElseThrow(CouponNotFoundException::new).toDomain(),
                entity.isUsed()
        );
    }

    public List<MemberCoupon> findAllByMemberId(final Long memberId) {
        final List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findByMemberId(memberId);
        return memberCouponEntities.stream()
                .map(this::getMemberCoupon)
                .collect(Collectors.toList());
    }
}
