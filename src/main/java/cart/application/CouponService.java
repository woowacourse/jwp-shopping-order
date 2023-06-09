package cart.application;

import static java.util.stream.Collectors.toList;

import cart.dto.response.MemberCouponResponse;
import cart.repository.CouponRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Transactional(readOnly = true)
    public List<MemberCouponResponse> findAllByMemberId(Long memberId) {
        return couponRepository.findAllByMemberId(memberId).stream()
                .map(MemberCouponResponse::from)
                .collect(toList());
    }
}
