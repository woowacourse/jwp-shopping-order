package cart.repository;

import static java.util.stream.Collectors.toList;

import cart.dao.MemberCouponDao;
import cart.domain.cart.MemberCoupon;
import cart.entity.MemberCouponEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class MemberCouponRepository {

    private final MemberCouponDao memberCouponDao;

    public MemberCouponRepository(
            final MemberCouponDao memberCouponDao
    ) {
        this.memberCouponDao = memberCouponDao;
    }

    public void saveAll(final List<MemberCoupon> memberCoupons) {
        final List<MemberCouponEntity> memberCouponEntities = memberCoupons.stream()
                .map(MemberCouponEntity::from)
                .collect(toList());

        memberCouponDao.insertAll(memberCouponEntities);
    }
}
