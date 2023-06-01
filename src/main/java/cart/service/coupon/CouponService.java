package cart.service.coupon;

import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.dto.coupon.CouponCreateRequest;
import cart.dto.coupon.CouponResponse;
import cart.exception.CouponNotFoundException;
import cart.exception.MemberNotFoundException;
import cart.repository.coupon.CouponRepository;
import cart.repository.coupon.MemberCouponRepository;
import cart.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final MemberCouponRepository memberCouponRepository;

    public CouponService(final CouponRepository couponRepository, final MemberRepository memberRepository, final MemberCouponRepository memberCouponRepository) {
        this.couponRepository = couponRepository;
        this.memberRepository = memberRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    public List<CouponResponse> findAllCoupons() {
        return couponRepository.findAll().getCoupons().stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }

    public CouponResponse findById(final Long couponId) {
        return CouponResponse.from(couponRepository.findById(couponId));
    }

    public long createCoupon(final CouponCreateRequest request) {
        Coupon coupon = Coupon.create(request.getName(), request.getIsPercentage(), request.getAmount());
        return couponRepository.save(request.getName(), request.getIsPercentage(), request.getAmount());
    }

    public void deleteCoupon(final Long couponId) {
        couponRepository.deleteById(couponId);
    }

    public void giveCouponToMember(final Long couponId, final Long memberId) {
        validateRequest(couponId, memberId);

        Coupon giftCoupon = couponRepository.findById(couponId);
        Member member = memberRepository.findMemberById(memberId);

        memberCouponRepository.save(member, giftCoupon);
    }

    private void validateRequest(final Long couponId, final Long memberId) {
        if (!memberRepository.isExistMemberById(memberId)) {
            throw new MemberNotFoundException();
        }

        if (!couponRepository.isExistCouponById(couponId)) {
            throw new CouponNotFoundException();
        }
    }
}
