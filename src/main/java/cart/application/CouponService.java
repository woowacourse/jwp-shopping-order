package cart.application;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.Coupon;
import cart.domain.CouponType;
import cart.domain.Member;
import cart.dto.CouponCreateRequest;
import cart.dto.CouponResponse;
import cart.dto.CouponResponses;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponDao couponDao;
    private final MemberCouponDao memberCouponDao;

    public CouponService(CouponDao couponDao, MemberCouponDao memberCouponDao) {
        this.couponDao = couponDao;
        this.memberCouponDao = memberCouponDao;
    }

    public Coupon findById(long couponId) {
        return couponDao.findById(couponId);
    }

    public void issueCouponByIdToMember(long couponId, Member member) {
        memberCouponDao.save(member.getId(), couponId);
    }

    public CouponResponses findAll() {
        return new CouponResponses(couponDao.findAll().stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList())
        );
    }

    public CouponResponses findByMember(Member member) {
        List<Long> couponIds = memberCouponDao.findCouponIdsByMemberId(member.getId());
        return new CouponResponses(couponIds.stream()
                .map(couponDao::findById)
                .map(CouponResponse::from)
                .collect(Collectors.toList())
        );
    }

    public CouponResponse createCoupon(CouponCreateRequest couponCreateRequest) {
        Coupon couponToSave = new Coupon(
                couponCreateRequest.getName(),
                CouponType.from(couponCreateRequest.getType()),
                couponCreateRequest.getAmount()
        );
        Coupon couponAfterSave = couponDao.save(couponToSave);
        return CouponResponse.from(couponAfterSave);
    }
}
