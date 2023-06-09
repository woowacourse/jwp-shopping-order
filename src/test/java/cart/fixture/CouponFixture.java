package cart.fixture;

import cart.dao.entity.CouponEntity;
import cart.domain.coupon.*;

import java.time.LocalDateTime;

public class CouponFixture {
     public static final Coupon 테스트쿠폰1 = new PercentageCoupon(new CouponInfo(1L, "테스트쿠폰1", 10000, 3000), 0.2);
     public static final CouponEntity 테스트쿠폰1_엔티티 = new CouponEntity(1L, "테스트쿠폰1", 10000, 3000, "FIXED_PERCENTAGE", null, 0.2);
     public static final Coupon 테스트쿠폰2 = new AmountCoupon(new CouponInfo(2L, "테스트쿠폰2", 15000, null), 2000);
     public static final CouponEntity 테스트쿠폰2_엔티티 = new CouponEntity(2L, "테스트쿠폰2", 15000, null, "FIXED_AMOUNT", 2000, null);

     public static final MemberCoupon 회원쿠폰1= new UsableMemberCoupon(1L, 테스트쿠폰1, MemberFixture.라잇, LocalDateTime.now().plusDays(7), LocalDateTime.now());
     public static final MemberCoupon 회원쿠폰2= new UsableMemberCoupon(1L, 테스트쿠폰2, MemberFixture.엽토, LocalDateTime.now().plusDays(7), LocalDateTime.now());
     public static final MemberCoupon 회원쿠폰3= new UsedMemberCoupon(1L, 테스트쿠폰2, MemberFixture.엽토, LocalDateTime.now().plusDays(7), LocalDateTime.now());
     public static final MemberCoupon 회원쿠폰1_아이디_널= new UsableMemberCoupon(null, 테스트쿠폰1, MemberFixture.라잇, LocalDateTime.now().plusDays(7), null);
     public static final MemberCoupon 회원쿠폰2_아이디_널= new UsableMemberCoupon(null, 테스트쿠폰2, MemberFixture.엽토, LocalDateTime.now().plusDays(7), null);
     public static final MemberCoupon 회원쿠폰3_아이디_널= new UsedMemberCoupon(null, 테스트쿠폰2, MemberFixture.엽토, LocalDateTime.now().plusDays(7), null);
}
