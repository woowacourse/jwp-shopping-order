package cart.application;

import cart.domain.Coupon;
import cart.domain.Member;
import cart.dto.CouponResponse;
import cart.exception.CouponException;
import cart.repository.CouponRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public List<CouponResponse> findByMember(final Member member) {
        return CouponResponse.from(couponRepository.findUsableByMember(member.getId()));
    }

    public Coupon checkId(final long memberId, final long id) {
        return couponRepository.findByIdForMember(memberId, id)
                .orElseThrow(() -> new CouponException.IllegalId(id));
    }
}
