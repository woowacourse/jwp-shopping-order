package shop.persistence.repository;

import org.springframework.stereotype.Repository;
import shop.domain.coupon.Coupon;
import shop.domain.coupon.MemberCoupon;
import shop.domain.member.EncryptedPassword;
import shop.domain.member.Member;
import shop.domain.member.MemberName;
import shop.domain.repository.MemberCouponRepository;
import shop.persistence.entity.detail.MemberCouponDetail;
import shop.persistence.dao.MemberCouponDao;
import shop.persistence.entity.MemberCouponEntity;

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
                memberCoupon.getMemberId(),
                memberCoupon.getCouponId(),
                memberCoupon.getIssuedAt(),
                memberCoupon.getMemberCouponExpiredAt(),
                memberCoupon.isUsed()
        );
    }

    @Override
    public MemberCoupon findByMemberIdAndCouponId(Long memberId, Long couponId) {
        MemberCouponDetail memberCouponDetail =
                memberCouponDao.findByMemberIdAndCouponId(memberId, couponId);

        return toMemberCoupon(memberCouponDetail);
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
