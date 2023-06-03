package cart.service;

import cart.controller.response.CouponResponseDto;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.repository.CouponRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }


    public List<CouponResponseDto> issue(final Member member) {
        List<MemberCoupon> memberCoupons = couponRepository.saveCoupon(member);

        return memberCoupons.stream()
                .map(CouponResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<CouponResponseDto> findAll(final Member member) {
        List<MemberCoupon> memberCouponByMember = couponRepository.findMemberCouponByMember(member);

        return memberCouponByMember.stream()
                .map(CouponResponseDto::from)
                .collect(Collectors.toList());
    }

}
