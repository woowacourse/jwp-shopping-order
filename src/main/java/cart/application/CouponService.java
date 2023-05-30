package cart.application;

import static java.util.stream.Collectors.toList;

import cart.dao.CouponDao;
import cart.dto.CouponResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponDao couponDao;

    public CouponService(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    public List<CouponResponse> findAllByMemberId(Long memberId) {
        return couponDao.findAllByMemberId(memberId).stream()
                .map(CouponResponse::from)
                .collect(toList());
    }
}
