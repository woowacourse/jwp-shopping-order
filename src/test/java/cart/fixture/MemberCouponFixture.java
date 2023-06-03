package cart.fixture;

import cart.persistence.entity.MemberCouponEntity;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MemberCouponFixture {

    public static MemberCouponEntity 유효한_멤버쿠폰_엔티티(long memberId, long couponId) {
        return new MemberCouponEntity(memberId, couponId, false, Timestamp.valueOf("9999-12-31 00:00:00"),
                Timestamp.valueOf(LocalDateTime.now()));
    }

    public static MemberCouponEntity 만료된_멤버쿠폰_엔티티(long memberId, long couponId) {
        return new MemberCouponEntity(memberId, couponId, false, Timestamp.valueOf("2000-01-01 00:00:00"),
                Timestamp.valueOf(LocalDateTime.now()));
    }

    public static MemberCouponEntity 사용된_멤버쿠폰_엔티티(long memberId, long couponId) {
        return new MemberCouponEntity(memberId, couponId, true, Timestamp.valueOf("9999-12-31 00:00:00"),
                Timestamp.valueOf(LocalDateTime.now()));
    }
}
