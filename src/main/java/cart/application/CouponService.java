package cart.application;

import cart.domain.Member;
import cart.domain.repository.CouponRepository;
import cart.dto.request.CouponCreateRequest;
import cart.dto.response.CouponResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {
    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Long publishUserCoupon(Member member, CouponCreateRequest request) {
        return couponRepository.publishUserCoupon(member, request.getId());
    }

    public List<CouponResponse> getUserCoupon(Member member) {
        return couponRepository.getUserCoupon(member).stream()
                .map(it -> new CouponResponse(it.getId(), it.getName(),
                        it.getCouponTypes().getCouponTypeName(),
                        it.getMinimumPrice(), it.getDiscountRate(),it.getDiscountPrice()
                ))
                .collect(Collectors.toList());
    }
}
