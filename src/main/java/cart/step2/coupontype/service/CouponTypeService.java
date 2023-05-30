package cart.step2.coupontype.service;

import cart.step2.coupontype.domain.repository.CouponTypeRepository;
import cart.step2.coupontype.presentation.dto.CouponTypeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CouponTypeService {

    private final CouponTypeRepository couponTypeRepository;

    public CouponTypeService(final CouponTypeRepository couponTypeRepository) {
        this.couponTypeRepository = couponTypeRepository;
    }

    public List<CouponTypeResponse> getCouponsType() {
        return couponTypeRepository.findAll().stream()
                .map(CouponTypeResponse::new)
                .collect(Collectors.toList());
    }

}
