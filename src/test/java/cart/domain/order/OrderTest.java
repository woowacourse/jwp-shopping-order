package cart.domain.order;

import cart.domain.coupon.*;
import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Nickname;
import cart.domain.member.Password;
import cart.domain.product.Product;
import cart.exception.OrderException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Member member1;
    private Member member2;
    private Product product2;
    private Product product1;
    private MemberCoupon memberCoupon;
    private List<OrderItem> orderItems;
    private Order order;

    @BeforeEach
    void setup() {
        member1 = new Member(1L, new Email("a@a.aa"), new Password("1234"), new Nickname("1번"));
        member2 = new Member(2L, new Email("b@a.aa"), new Password("1234"), new Nickname("2번"));
        product1 = new Product(1L, "테스트1", 1000, "이미지주소");
        product2 = new Product(2L, "테스트2", 2000, "이미지주소");
        memberCoupon = new UsedMemberCoupon(1L, new AmountCoupon(new CouponInfo(1L, "1000원 쿠폰", 3000, 1000), 1000), member1, LocalDateTime.MAX, LocalDateTime.now());
        orderItems = new ArrayList<>();
        orderItems.add(new OrderItem(1L, product1, 2));
        orderItems.add(new OrderItem(2L, product2, 3));
        order = new Order(1L, ShippingFee.BASIC, orderItems, memberCoupon, member1);
    }

    @Test
    public void 전체_금액을_계산한다() {
        int totalPrice = order.calculateTotalPrice();

        Assertions.assertThat(totalPrice).isEqualTo(2 * 1000 + 3 * 2000);
    }

    @Test
    public void 할인된_금액을_계산한다() {
        int discountPrice = order.calculateDiscountPrice();

        Assertions.assertThat(discountPrice).isEqualTo(1000);
    }

    @Test
    public void 회원이_맞는지_체크한다() {
        assertDoesNotThrow(() -> order.checkOwner(member1));
    }


    @Test
    public void 회원이_일치하지_않으면_예외를_발생한다() {
        assertThatThrownBy(() -> order.checkOwner(member2))
                .isInstanceOf(OrderException.IllegalMember.class);
    }

    @Test
    public void 주문을_취소한다() {
        Order canceledOrder = order.cancel();
        Assertions.assertThat(canceledOrder.getMemberCoupon()).isInstanceOf(UsableMemberCoupon.class);
    }

}
