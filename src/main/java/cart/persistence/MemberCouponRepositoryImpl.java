package cart.persistence;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.member.EncryptedPassword;
import cart.domain.member.Member;
import cart.domain.member.MemberName;
import cart.domain.repository.MemberCouponRepository;
import cart.persistence.dao.MemberCouponDao;
import cart.persistence.entity.MemberCouponEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MemberCouponRepositoryImpl implements MemberCouponRepository {
    private final MemberCouponDao memberCouponDao;

    public MemberCouponRepositoryImpl(MemberCouponDao memberCouponDao) {
        this.memberCouponDao = memberCouponDao;
    }

    @Override
    public Long save(MemberCoupon memberCoupon) {
        MemberCouponEntity memberCouponEntity = toEntity(memberCoupon);

        return memberCouponDao.insert(memberCouponEntity);
    }

    private MemberCouponEntity toEntity(MemberCoupon memberCoupon) {
        return new MemberCouponEntity(
                memberCoupon.getUserId(),
                memberCoupon.getCouponId(),
                memberCoupon.getIssuedAt(),
                memberCoupon.getMemberCouponExpiredAt(),
                memberCoupon.isUsed()
        );
    }

    @Override
    public List<MemberCoupon> findAllByMemberId(Long memberId) {
        List<MemberCouponDetail> memberCouponDetails = memberCouponDao.findAllByMemberId(memberId);

        return memberCouponDetails.stream()
                .map(this::toMemberCoupon)
                .collect(Collectors.toList());
    }

    private MemberCoupon toMemberCoupon(MemberCouponDetail memberCouponDetail) {
        Member member = toMember(memberCouponDetail);
        Coupon coupon = toCoupon(memberCouponDetail);

        return new MemberCoupon(
                memberCouponDetail.getId(),
                member,
                coupon,
                memberCouponDetail.getIssuedAt(),
                memberCouponDetail.getMemberCouponExpiredAt(),
                memberCouponDetail.isUsed()
        );
    }

    private Member toMember(MemberCouponDetail memberCouponDetail) {
        return new Member(
                memberCouponDetail.getMemberId(),
                new MemberName(memberCouponDetail.getMemberName()),
                new EncryptedPassword(memberCouponDetail.getPassword())
        );
    }

    private Coupon toCoupon(MemberCouponDetail memberCouponDetail) {
        return new Coupon(
                memberCouponDetail.getCouponId(),
                memberCouponDetail.getCouponName(),
                memberCouponDetail.getDiscountRate(),
                memberCouponDetail.getPeriod(),
                memberCouponDetail.getCouponExpiredAt()
        );
    }

    @Override
    public int updateCouponUsingStatus(Long memberCouponId, Boolean isUsed) {
        return memberCouponDao.updateCouponUseStatus(memberCouponId, isUsed);
    }
}
