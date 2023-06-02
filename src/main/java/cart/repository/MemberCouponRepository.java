package cart.repository;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableList;

import cart.dao.MemberCouponDao;
import cart.dao.dto.MemberCouponDto;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.exception.MemberCouponNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponRepository {

    private final MemberCouponDao memberCouponDao;
    private final CouponRepository couponRepository;

    public MemberCouponRepository(final MemberCouponDao memberCouponDao, final CouponRepository couponRepository) {
        this.memberCouponDao = memberCouponDao;
        this.couponRepository = couponRepository;
    }

    public MemberCoupon insert(final MemberCoupon memberCoupon) {
        final Long memberCouponId = memberCouponDao.insert(MemberCouponDto.from(memberCoupon));
        return new MemberCoupon(memberCouponId, memberCoupon.getCoupon(), memberCoupon.getMemberId());
    }

    public List<MemberCoupon> findByMemberId(final Long memberId) {
        final List<MemberCouponDto> memberCouponDtos = memberCouponDao.findByMemberId(memberId);
        final Map<Long, MemberCouponDto> couponIdMap = memberCouponDtos.stream()
                .collect(toMap(MemberCouponDto::getCouponId, Function.identity()));
        return couponRepository.findByIds(couponIdMap.keySet()).stream()
                .map(coupon -> new MemberCoupon(couponIdMap.get(coupon.getId()).getId(), coupon, memberId))
                .collect(toUnmodifiableList());
    }

    public MemberCoupon findUsedCouponById(final Long id) {
        final MemberCouponDto memberCouponDto = memberCouponDao.findUsedCouponById(id)
                .orElseThrow(MemberCouponNotFoundException::new);
        final Coupon coupon = couponRepository.findById(memberCouponDto.getCouponId());
        return new MemberCoupon(memberCouponDto.getId(), coupon, memberCouponDto.getMemberId());
    }

    public MemberCoupon findUnUsedCouponById(final Long id) {
        final MemberCouponDto memberCouponDto = memberCouponDao.findUnUsedCouponById(id)
                .orElseThrow(MemberCouponNotFoundException::new);
        final Coupon coupon = couponRepository.findById(memberCouponDto.getCouponId());
        return new MemberCoupon(memberCouponDto.getId(), coupon, memberCouponDto.getMemberId());
    }

    public void useMemberCoupon(final MemberCoupon memberCoupon) {
        memberCouponDao.updateUsedTrue(memberCoupon.getId());
    }
}
