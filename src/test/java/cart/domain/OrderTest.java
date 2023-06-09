package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.exception.CouponException;
import cart.exception.OrderException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private static final Coupon COUPON_USABLE = new Coupon(1L,
            new CouponType(1L, "배송비 쿠폰", DiscountType.DISCOUNT_PRICE, new Money(3000L)), false);
    private static final Coupon COUPON_USED = new Coupon(1L,
            new CouponType(1L, "배송비 쿠폰", DiscountType.DISCOUNT_PRICE, new Money(3000L)), true);
    private static final List<OrderItem> ORDER_ITEMS = List.of(
            new OrderItem(1L, "item1", new Money(1000L), "image.png", 1),
            new OrderItem(2L, "item2", new Money(3000L), "image2.png", 2));

    @DisplayName("주문을 완료할 때 쿠폰을 사용한다.")
    @Test
    void complete() {
        // given
        final Order order = new Order(COUPON_USABLE, new Money(3000L), ORDER_ITEMS);

        // when
        final long totalPrice = ORDER_ITEMS.stream()
                .mapToLong(orderItem -> orderItem.getQuantity() * orderItem.getPrice())
                .sum();
        final Order completed = order.complete(new Money(totalPrice));

        // then
        assertThat(completed.getCoupon().isUsed()).isTrue();
    }

    @DisplayName("주문을 완료할 때 이미 사용한 쿠폰을 적용하면 예외를 던진다.")
    @Test
    void completeFailUsedCoupon() {
        // given
        final Order order = new Order(COUPON_USED, new Money(3000L), ORDER_ITEMS);

        // when, then
        final long totalPrice = ORDER_ITEMS.stream()
                .mapToLong(orderItem -> orderItem.getQuantity() * orderItem.getPrice())
                .sum();
        assertThatThrownBy(() -> order.complete(new Money(totalPrice)))
                .isInstanceOf(CouponException.AlreadyUsed.class);
    }

    @DisplayName("주문을 완료할 때 총 금액이 일치하지 않으면 예외를 던진다.")
    @Test
    void completeFailWrongTotalPrice() {
        // given
        final Order order = new Order(COUPON_USABLE, new Money(3000L), ORDER_ITEMS);

        // when, then
        assertThatThrownBy(() -> order.complete(new Money(500L)))
                .isInstanceOf(OrderException.OutOfDatedProductPrice.class);
    }

    @DisplayName("주문을 취소할 때 쿠폰을 환불한다.")
    @Test
    void cancel() {
        // given
        final Order order = new Order(COUPON_USED, new Money(3000L), ORDER_ITEMS);

        // when
        final Order canceled = order.cancel();

        // then
        assertThat(canceled.getCoupon().isUsed()).isFalse();
    }

    @DisplayName("주문을 취소할 때 쿠폰이 미사용 상태이면 예외를 던진다.")
    @Test
    void cancelFailAbnormalCouponStatus() {
        // given
        final Order order = new Order(COUPON_USABLE, new Money(3000L), ORDER_ITEMS);

        // when, then
        assertThatThrownBy(order::cancel)
                .isInstanceOf(CouponException.AlreadyUsable.class);
    }

    @DisplayName("쿠폰 적용 여부를 반환한다.")
    @Test
    void hasCoupon() {
        // given
        final Order hasCoupon = new Order(COUPON_USABLE, new Money(3000L), ORDER_ITEMS);
        final Order noCoupon = new Order(null, new Money(3000L), ORDER_ITEMS);

        // when, then
        assertThat(hasCoupon.hasCoupon()).isTrue();
        assertThat(noCoupon.hasCoupon()).isFalse();
    }

    @DisplayName("상품 총 금액을 계산한다.")
    @Test
    void totalProductPrice() {
        // given
        final Order order = new Order(COUPON_USABLE, new Money(3000L), ORDER_ITEMS);

        // when
        final long totalPrice = ORDER_ITEMS.stream()
                .mapToLong(orderItem -> orderItem.getQuantity() * orderItem.getPrice())
                .sum();
        assertThat(order.totalProductPrice().getValue())
                .isEqualTo(totalPrice);
    }

    @DisplayName("주문 총 결제 금액을 계산한다.")
    @Test
    void totalPayments() {
        // given
        final long deliveryFee = 3000L;
        final long discountAmount = 1000L;
        final Order order = new Order(
                new Coupon(1L,
                        new CouponType(1L, "1000원 할인 쿠폰", DiscountType.DISCOUNT_PRICE, new Money(discountAmount)),
                        false),
                new Money(deliveryFee),
                ORDER_ITEMS);

        // when
        final long totalPrice = ORDER_ITEMS.stream()
                .mapToLong(orderItem -> orderItem.getQuantity() * orderItem.getPrice())
                .sum();
        assertThat(order.totalPayments().getValue())
                .isEqualTo(totalPrice + deliveryFee - discountAmount);
    }
}
