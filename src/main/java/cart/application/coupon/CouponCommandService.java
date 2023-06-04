package cart.application.coupon;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.coupon.dto.CouponRequest;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponRepository;
import cart.domain.coupon.MemberCouponRepository;
import cart.domain.coupon.SerialNumber;
import cart.domain.coupon.SerialNumberRepository;
import cart.domain.coupon.type.CouponInfo;

@Transactional
@Service
public class CouponCommandService {

	private final CouponRepository couponRepository;
	private final MemberCouponRepository memberCouponRepository;
	private final SerialNumberRepository serialNumberRepository;

	public CouponCommandService(final CouponRepository couponRepository,
		final MemberCouponRepository memberCouponRepository, final SerialNumberRepository serialNumberRepository) {
		this.couponRepository = couponRepository;
		this.memberCouponRepository = memberCouponRepository;
		this.serialNumberRepository = serialNumberRepository;
	}

	public Long createCoupon(final CouponRequest request) {
		final Long savedId = couponRepository.createCoupon(
			CouponInfo.of(null, request.getCouponType(), request.getName(), request.getDiscount()));
		final List<SerialNumber> serialNumbers = SerialNumber.generateSerialNumbers(request.getCouponCount());

		serialNumberRepository.generateCouponSerialNumber(savedId, serialNumbers);
		return savedId;
	}

	public Long addCoupon(final Long memberId, final Long couponId) {
		final Coupon coupon = couponRepository.findCouponById(couponId);
		final SerialNumber serialNumber = coupon.findUnissuedSerialNumber()
			.orElseThrow(() -> new IllegalArgumentException("남은 시리얼 넘버가 없습니다."));

		return memberCouponRepository.addCoupon(memberId, serialNumber.getId());
	}

	public void updateCoupon(final Long couponId, final CouponRequest request) {
		final CouponInfo couponInfo = CouponInfo.of(couponId, request.getCouponType(), request.getName(),
			request.getDiscount());
		couponRepository.updateCouponInfo(couponInfo);
	}

	public void generateExtraCoupons(final Long couponId, final CouponRequest request) {
		final List<SerialNumber> serialNumbers = SerialNumber.generateSerialNumbers(request.getCouponCount());
		serialNumberRepository.generateCouponSerialNumber(couponId, serialNumbers);
	}

	public void removeCoupon(final Long couponId) {
		couponRepository.removeCoupon(couponId);
	}
}
