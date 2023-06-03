package cart.application;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.repository.CouponRepository;
import cart.domain.repository.MemberCouponRepository;
import cart.dto.response.ActiveCouponResponse;
import cart.dto.response.CouponDiscountResponse;
import cart.dto.response.CouponResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public CouponService(final CouponRepository couponRepository, final MemberCouponRepository memberCouponRepository) {
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    public List<CouponResponse> findAllByMember(final Member member) {
        final List<Coupon> coupons = couponRepository.findAll();
        final List<Long> memberCouponIds = memberCouponRepository.findCouponIdsByMemberId(member.getId());
        final List<CouponResponse> couponResponses = new ArrayList<>();
        for (Coupon coupon : coupons) {
            final Optional<Long> publishOptional = memberCouponIds.stream()
                    .filter(it -> it.equals(coupon.getId()))
                    .findAny();
            couponResponses.add(new CouponResponse(coupon.getId(), coupon.getName(), coupon.getMinAmount().getValue(),
                    coupon.getDiscountAmount().getValue(), publishOptional.isPresent()));
        }
        return couponResponses;
    }

    public void publish(final Member member, final Long id) {
        memberCouponRepository.create(id, member.getId());
    }

    public List<ActiveCouponResponse> findActiveCoupons(final Member member, final int totalProductAmount) {
        final List<Long> memberCouponIds = memberCouponRepository.findCouponIdsByMemberId(member.getId());
        final List<Coupon> coupons = couponRepository.findAll();
        final List<Coupon> filteredCoupons = coupons.stream()
                .filter(it -> memberCouponIds.contains(it.getId())
                        && it.getMinAmount().getValue() <= totalProductAmount)
                .collect(Collectors.toList());
        return filteredCoupons.stream()
                .map(it -> new ActiveCouponResponse(it.getId(), it.getName(), it.getMinAmount().getValue()))
                .collect(Collectors.toList());
    }

    public CouponDiscountResponse findCouponDiscountAmount(final Long id, final int total) {
        final Coupon coupon = couponRepository.findById(id);
        final int discountedAmount = coupon.calculateDiscountedAmount(total);
        final int discountAmount = coupon.getDiscountAmount().getValue();
        return new CouponDiscountResponse(discountedAmount, discountAmount);
    }
}
