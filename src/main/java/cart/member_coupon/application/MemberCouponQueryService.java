package cart.member_coupon.application;

import cart.member.domain.Member;
import cart.member_coupon.dao.MemberCouponDao;
import cart.member_coupon.dao.MemberCouponEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberCouponQueryService {

  private final MemberCouponDao memberCouponDao;

  public MemberCouponQueryService(final MemberCouponDao memberCouponDao) {
    this.memberCouponDao = memberCouponDao;
  }

  public List<Long> searchCouponsIdOwenByMember(final Member member) {
    List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findByMemberId(member.getId());

    return memberCouponEntities.stream()
        .map(MemberCouponEntity::getCouponId)
        .collect(Collectors.toList());
  }
}
