package cart.coupon.application;

import cart.cart.Cart;
import cart.cartitem.CartItem;
import cart.deliveryprice.DeliveryPrice;
import cart.discountpolicy.application.DiscountPolicyRepository;
import cart.discountpolicy.application.DiscountPolicyService;
import cart.discountpolicy.discountcondition.DiscountCondition;
import cart.discountpolicy.discountcondition.DiscountTarget;
import cart.discountpolicy.discountcondition.DiscountUnit;
import cart.member.Member;
import cart.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SaleServiceTest {
    private CouponService couponService;
    private CouponRepository couponRepository;

    @BeforeEach
    void setup() {
        this.couponRepository = new CouponRepository();
        this.couponService = new CouponService(new DiscountPolicyService(new DiscountPolicyRepository()), couponRepository);
    }

    @Test
    @DisplayName("할인 조건, 쿠폰 이름을 제공하면서 쿠폰을 새로 만들 수 있어요.")
    void saveCoupon() {
        final var discountCondition = DiscountCondition.from(DiscountTarget.DELIVERY, DiscountUnit.ABSOLUTE, 1500);
        final var couponId = couponService.saveCoupon(discountCondition, "배송비 50% 할인 쿠폰");
        assertThat(couponId).isEqualTo(1L);
    }

    @Test
    @DisplayName("담겨있는 장바구니 정보에 쿠폰의 할인정책을 적용할 수 있어요.")
    void applyCoupon() {
        final var cart = getTestCart();

        final var discountCondition = DiscountCondition.from(DiscountTarget.DELIVERY, DiscountUnit.ABSOLUTE, 1500);
        final var couponId = couponService.saveCoupon(discountCondition, "배송비 50% 할인 쿠폰");

        couponService.applyCoupon(couponId, cart);

        assertThat(cart.getCartItems())
                .extracting(CartItem::getDiscountPrice)
                .containsExactly(0, 0);
        assertThat(cart.getDeliveryPrice().getPrice())
                .isEqualTo(1500);
    }

    public Cart getTestCart() {
        final var 백여우 = new Member(1L, "fox@gmail.com", "1234");
        final var 피자 = new Product(1L, "피자", 20_000, "img");
        final var 치킨 = new Product(2L, "치킨", 30_000, "img");

        final var 백여우가담은피자 = new CartItem(1L, 3, 피자, 백여우);
        final var 백여우가담은치킨 = new CartItem(2L, 2, 치킨, 백여우);

        return new Cart(
                List.of(백여우가담은피자, 백여우가담은치킨),
                new DeliveryPrice()
        );
    }
}