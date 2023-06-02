package cart.coupon.application;

import cart.coupon.application.dto.CouponResponse;
import cart.coupon.dao.CouponDao;
import cart.coupon.dao.CouponEntity;
import cart.member.domain.Member;
import cart.member_coupon.application.MemberCouponQueryService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CouponQueryService {

  private final CouponDao couponDao;
  private final MemberCouponQueryService memberCouponQueryService;

  public CouponQueryService(final CouponDao couponDao, final MemberCouponQueryService memberCouponQueryService) {
    this.couponDao = couponDao;
    this.memberCouponQueryService = memberCouponQueryService;
  }

  public List<CouponResponse> searchCoupons(final Member member) {

    //member_coupon 에서 member가 가지고 있는 쿠폰을 조회
    final List<Long> couponIds = memberCouponQueryService.searchCouponsIdOwenByMember(member);

    //그 아이디를 통해서 쿠폰들의 정보를 조회
    List<CouponEntity> couponEntities = couponDao.findByIdsIn(couponIds);

    return couponEntities.stream()
        .map(it -> new CouponResponse(
            it.getId(),
            it.getName(),
            it.getDiscountPrice()
        ))
        .collect(Collectors.toList());
  }
}
