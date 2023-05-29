package cart.application;

import cart.domain.Coupons;
import cart.domain.Member;
import cart.dto.CouponResponse;
import cart.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@Service
public class CouponProvider {

    private final CouponRepository couponRepository;

    public CouponProvider(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<CouponResponse> findCouponByMember(final Member member) {
        final Coupons coupons = couponRepository.findCouponsByMemberId(member.getId());
        return coupons.getCoupons().stream()
                .map(it -> new CouponResponse(
                        it.getId(),
                        it.getName(),
                        it.getDiscountAmount(),
                        it.getDescription(),
                        it.isUsageStatus()))
                .collect(toList());
    }
}
