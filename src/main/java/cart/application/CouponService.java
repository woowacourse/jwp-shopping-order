package cart.application;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.repository.CouponRepository;
import cart.domain.repository.MemberCouponRepository;
import cart.dto.response.CouponResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
}
