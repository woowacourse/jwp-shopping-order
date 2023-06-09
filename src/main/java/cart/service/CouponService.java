package cart.service;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.vo.Price;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import cart.service.response.CouponResponse;
import cart.service.response.DiscountPriceResponse;
import cart.service.response.MemberCouponResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponService {

    private final MemberCouponRepository memberCouponRepository;
    private final CouponRepository couponRepository;

    public CouponService(final MemberCouponRepository memberCouponRepository, final CouponRepository couponRepository) {
        this.memberCouponRepository = memberCouponRepository;
        this.couponRepository = couponRepository;
    }

    public List<MemberCouponResponse> findMemberCoupons(final Member member) {
        final List<MemberCoupon> memberCoupons = memberCouponRepository.findByMemberId(member.getId());
        return memberCoupons.stream()
                .map(memberCoupon -> new MemberCouponResponse(memberCoupon.getId(), memberCoupon.getCoupon().getName()))
                .collect(toUnmodifiableList());
    }

    public DiscountPriceResponse discount(final Member member, final Integer originPrice, final Long memberCouponId) {
        final MemberCoupon memberCoupon = memberCouponRepository.findUnUsedCouponById(memberCouponId);
        memberCoupon.checkOwner(member);
        final Price origin = new Price(originPrice);
        final Price discountPrice = memberCoupon.discount(origin);
        return DiscountPriceResponse.of(discountPrice, origin.subtract(discountPrice));
    }

    @Transactional
    public Long issue(final Member member, final Long id) {
        final Coupon coupon = couponRepository.findById(id);
        final MemberCoupon memberCoupon = new MemberCoupon(coupon, member.getId());
        return memberCouponRepository.insert(memberCoupon).getId();
    }

    public List<CouponResponse> findAll() {
        return couponRepository.findAll().stream()
                .map(CouponResponse::from)
                .collect(toUnmodifiableList());
    }
}
