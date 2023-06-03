package cart.persistence.dao;

import cart.domain.order.BigDecimalConverter;
import cart.persistence.entity.CartEntity;
import cart.persistence.entity.CouponEntity;
import cart.persistence.entity.MemberCouponEntity;
import cart.persistence.entity.MemberEntity;
import cart.persistence.entity.OrderEntity;
import cart.persistence.entity.ProductEntity;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({OrderDao.class, MemberDao.class, CouponDao.class, MemberCouponDao.class, ProductDao.class, CartItemDao.class})
public class DaoTestHelper extends DaoTest {

    @Autowired
    OrderDao orderDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    CouponDao couponDao;
    @Autowired
    MemberCouponDao memberCouponDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    CartItemDao cartItemDao;

    Long 치킨_저장() {
        final ProductEntity 치킨 = new ProductEntity("치킨", "chicken_image_url", 20000, false);
        return productDao.insert(치킨);
    }

    Long 피자_저장() {
        final ProductEntity 피자 = new ProductEntity("피자", "pizza_image_url", 30000, false);
        return productDao.insert(피자);
    }


    Long 져니_저장() {
        final MemberEntity 져니 = new MemberEntity("journey", "password");
        return memberDao.insert(져니);
    }

    Long 장바구니_치킨_저장(final Long 저장된_져니_아이디, final Long 저장된_치킨_아이디) {
        final CartEntity 장바구니_치킨 = new CartEntity(저장된_져니_아이디, 저장된_치킨_아이디, 1);
        final Long 저장된_장바구니_치킨_아이디 = cartItemDao.insert(장바구니_치킨);
        return 저장된_장바구니_치킨_아이디;
    }

    Long 장바구니_피자_저장(final Long 저장된_져니_아이디, final Long 저장된_피자_아이디) {
        final CartEntity 장바구니_피자 = new CartEntity(저장된_져니_아이디, 저장된_피자_아이디, 10);
        final Long 저장된_장바구니_피자_아이디 = cartItemDao.insert(장바구니_피자);
        return 저장된_장바구니_피자_아이디;
    }

    Long 신규_가입_쿠폰_저장() {
        final LocalDateTime 쿠폰_발급_시간 = LocalDateTime.of(2023, 6, 1, 13, 0, 0);
        final CouponEntity 신규_가입_축하_쿠폰 = new CouponEntity("신규 가입 축하 쿠폰", 20, 10, 쿠폰_발급_시간.plusDays(10));
        return couponDao.insert(신규_가입_축하_쿠폰);
    }

    Long 행운의_쿠폰_저장() {
        final LocalDateTime 쿠폰_발급_시간 = LocalDateTime.of(2023, 6, 1, 13, 0, 0);
        final CouponEntity 행운의_쿠폰 = new CouponEntity("행운의 쿠폰", 10, 1, 쿠폰_발급_시간.plusDays(10));
        return couponDao.insert(행운의_쿠폰);
    }

    Long 져니_쿠폰_저장(final long 저장된_져니_아이디, final Long 저장된_신규_가입_축하_쿠폰_아이디) {
        final LocalDateTime 쿠폰_발급_시간 = LocalDateTime.of(2023, 6, 1, 13, 0, 0);
        final LocalDateTime 쿠폰_만료_시간 = 쿠폰_발급_시간.plusDays(10);
        final MemberCouponEntity 사용자_쿠폰_저장_엔티티 = new MemberCouponEntity(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디,
            쿠폰_발급_시간, 쿠폰_만료_시간, false);
        return memberCouponDao.insert(사용자_쿠폰_저장_엔티티);
    }

    Long 주문_저장(final Long 저장된_져니_아이디) {
        final LocalDateTime 주문_시간 = LocalDateTime.of(2023, 6, 1, 13, 0, 0);
        final OrderEntity 주문_엔티티 = new OrderEntity(저장된_져니_아이디, BigDecimalConverter.convert(10000),
            BigDecimalConverter.convert(9000), 3000, 주문_시간);
        return orderDao.insert(주문_엔티티);
    }
}
