package shop.persistence.dao;

import shop.persistence.entity.CouponEntity;
import shop.persistence.entity.MemberEntity;
import shop.persistence.entity.ProductEntity;

import java.time.LocalDateTime;

public class DaoTestFixture {
    static final MemberEntity member = new MemberEntity("쥬니", "1234");
    static final ProductEntity pizza = new ProductEntity("피자", 20000, "피자.com");
    static final ProductEntity chicken = new ProductEntity("치킨", 30000, "치킨.com");
    static final CouponEntity testCoupon = new CouponEntity("테스트용 쿠폰", 80,
            365, LocalDateTime.now().plusDays(1));
    static final CouponEntity freeCoupon = new CouponEntity("공짜 쿠폰", 100,
            365, LocalDateTime.now().plusDays(1));
}
