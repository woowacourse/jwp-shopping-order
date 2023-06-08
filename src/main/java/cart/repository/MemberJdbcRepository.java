package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberDao;
import cart.dao.entity.CouponTypeCouponResultMap;
import cart.dao.entity.MemberEntity;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import cart.domain.member.MemberRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private final MemberDao memberDao;
    private final CouponDao couponDao;

    public MemberJdbcRepository(final MemberDao memberDao, final CouponDao couponDao) {
        this.memberDao = memberDao;
        this.couponDao = couponDao;
    }

    public Member findMemberByMemberIdWithCoupons(final Long memberId) {
        final MemberEntity memberEntity = memberDao.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        final List<CouponTypeCouponResultMap> couponTypeCouponResultMap = couponDao.findByMemberId(memberId);

        return toDomain(memberEntity, couponTypeCouponResultMap);
    }

    private Member toDomain(final MemberEntity memberEntity, final List<CouponTypeCouponResultMap> resultMap) {
        final List<Coupon> coupons = mapToCoupon(resultMap);

        return new Member(
                memberEntity.getId(),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                new Coupons(coupons)
        );
    }

    private static List<Coupon> mapToCoupon(final List<CouponTypeCouponResultMap> couponTypeCouponResultMap) {
        return couponTypeCouponResultMap.stream()
                .map(entity -> new Coupon(
                        entity.getCouponId(),
                        entity.getCouponTypeId(),
                        entity.getName(),
                        entity.getDescription(),
                        entity.getDiscountAmount(),
                        entity.getUsageStatus()))
                .collect(toList());
    }
}
