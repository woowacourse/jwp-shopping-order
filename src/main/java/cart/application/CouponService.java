package cart.application;

import cart.domain.Member;
import cart.domain.coupon.Coupon;
import cart.domain.repository.CouponRepository;
import cart.domain.repository.MemberCouponRepository;
import cart.dto.request.CouponCreateRequest;
import cart.dto.response.CouponIssuableResponse;
import cart.dto.response.CouponResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CouponService {
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    public CouponService(CouponRepository couponRepository, MemberCouponRepository memberCouponRepository) {
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    public Long save(Member member, CouponCreateRequest request) {
        return couponRepository.save(member, request.getId());
    }

    @Transactional(readOnly = true)
    public List<CouponResponse> findByMemberId(Member member) {
        return couponRepository.findByMemberId(member).stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CouponIssuableResponse> findAll(Member member) {
        List<Coupon> coupons = couponRepository.findAll();
        List<Coupon> memberCoupons = memberCouponRepository.findAllByMemberId(member);

        return coupons.stream()
                .map(it -> CouponIssuableResponse.of(it, !memberCoupons.contains(it)))
                .collect(Collectors.toList());
    }
}
