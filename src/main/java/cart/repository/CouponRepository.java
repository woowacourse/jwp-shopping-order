package cart.repository;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.dao.MemberDao;
import cart.domain.Coupon;
import cart.domain.Member;
import cart.domain.MemberCoupon;
import cart.dto.CouponDto;
import cart.dto.MemberCouponDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CouponRepository {

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;
    private final MemberDao memberDao;

    public CouponRepository(final CouponDao couponDao,
                            final MemberCouponDao memberCouponDao,
                            final MemberDao memberDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
        this.memberDao = memberDao;
    }

    // TODO: 해당 Member 가 쿠폰을 추가하면, 기존에 있는 쿠폰 다 저장함 (쿠폰들 리스트)
    public List<MemberCoupon> saveCoupon(Member member) {
        Member memberByEmail = memberDao.getMemberByEmail(member.getEmail());
        List<CouponDto> couponDtos = couponDao.findAll();
        return saveMemberCoupons(memberByEmail, couponDtos);
    }

    private List<MemberCoupon> saveMemberCoupons(final Member memberByEmail, final List<CouponDto> couponDtos) {
        List<MemberCoupon> memberCoupons = new ArrayList<>();

        for (CouponDto couponDto : couponDtos) {
            MemberCouponDto memberCouponDto = new MemberCouponDto(memberByEmail.getId(), couponDto.getId());
            Long id = memberCouponDao.insert(memberCouponDto);
            Coupon coupon = couponDtoToCoupon(couponDto);
            memberCoupons.add(new MemberCoupon(id, coupon));
        }

        return memberCoupons;
    }

    private Coupon couponDtoToCoupon(final CouponDto couponDto) {
        return new Coupon(
                couponDto.getId(),
                couponDto.getName(),
                couponDto.getDiscountRate(),
                couponDto.getDiscountPrice()
        );
    }

    // TODO: Member 에 맞는 쿠폰 전체 조회 (그냥 MemberId 로 Member_Coupon 을 가져오고, 그 다음에 그것들을 가지고 Coupon 을 가져오면 됨)

    // TODO: Member 와 CouponId 가 주어졌을 때, 해당 Member에 맞는 Coupon를 가져옴, 즉 MemberCoupon 이 아닌 Coupon 을 가져와야함

}
