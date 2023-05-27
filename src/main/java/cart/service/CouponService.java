package cart.service;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.dto.CouponResponse;
import cart.repository.CouponRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }
    
    public List<CouponResponse> findAll() {
        return couponRepository.findAll().stream()
                .map(CouponResponse::from)
                .collect(toUnmodifiableList());
    }
}
