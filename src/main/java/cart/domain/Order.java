package cart.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Order {

    private final List<CartItem> cartItems;
    private final Member member;
    private final Money price;
    private final OrderState state;

    private Order(List<CartItem> cartItems, Member member, Money price) {
        validateOwner(cartItems, member);
        this.cartItems = cartItems;
        this.member = member;
        this.price = price;
        this.state = OrderState.ORDERED;
    }

    private Order(List<CartItem> cartItems, Member member, Money price, OrderState orderState) {
        validateOwner(cartItems, member);
        this.cartItems = cartItems;
        this.member = member;
        this.price = price;
        this.state = orderState;
    }

    public static Order of(List<CartItem> cartItems, Member member, int price) {
        return new Order(cartItems, member, Money.from(price));
    }

    public Money apply(Coupon coupon) {
        // TODO: 2023-05-31 쿠폰뿐만 아니라 포인트, 할인 등 적용해야 할 경우 이게 확장성있나?
        // TODO: 2023-06-01 쿠폰 적용 시 필요한 정보가 다양해질 수 있는데, 확장성 좋으려면 어케? 
        return coupon.apply(price);
    }

    private void validateOwner(List<CartItem> cartItems, Member member) {
        cartItems.stream().forEach(cartItem -> cartItem.checkOwner(member));
    }

    public Order confirmOrder(Money deliveryFee, Money discountingPrice) {
        Money totalPrice = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(Money.from(0), Money::plus);

        Money calculated = totalPrice.minus(discountingPrice).plus(deliveryFee);
        System.out.println("total" + totalPrice.toInt() + "!!!!!!!!");
        System.out.println("discount" +discountingPrice.toInt() + "!!!!!!!!");
        System.out.println("calculate" +calculated.toInt() + "!!!!!!!!");
        System.out.println("!!!!!!!!" + this.price.toInt());

        if (!this.price.equals(calculated)) {
            throw new RuntimeException("주문 금액과 실제 계산예정금액이 불일치합니다.");
        }
        return new Order(this.cartItems, this.member, this.price, OrderState.CONFIRMED);
    }

    public List<Long> getCartItemIds() {
        return this.cartItems.stream().map(CartItem::getId).collect(Collectors.toList());
    }

    public OrderState getState() {
        return state;
    }

    public Member getMember() {
        return member;
    }

    public Money getPrice() {
        return price;
    }

    enum OrderState {
        ORDERED, CONFIRMED
    }


}
