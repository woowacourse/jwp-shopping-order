package cart.domain;

import static java.util.stream.Collectors.toList;

import java.util.List;

public class Order {

    private final Long id;
    private final Member member;
    private final List<Item> items;
    private final Money deliveryFee;

    public Order(Member member, List<Item> items, int deliveryFee) {
        this(null, member, items, new Money(deliveryFee));
    }

    public Order(Long id, Member member, List<Item> items, int deliveryFee) {
        this(id, member, items, new Money(deliveryFee));
    }

    public Order(Long id, Member member, List<Item> items, Money deliveryFee) {
        this.id = id;
        this.member = member;
        this.items = items;
        this.deliveryFee = deliveryFee;
    }

    public static Order of(Member member, List<CartItem> cartItems, int deliveryFee) {
        validateOwner(member, cartItems);
        List<Item> items = cartItems.stream()
                .map(CartItem::getItem)
                .collect(toList());
        return new Order(null, member, items, deliveryFee);
    }

    private static void validateOwner(Member member, List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            cartItem.checkOwner(member);
        }
    }

    public Money calculateTotalPrice() {
        Money totalCartsPrice = items.stream()
                .map(Item::calculateItemPrice)
                .reduce(new Money(0), Money::add);
        return totalCartsPrice.add(deliveryFee);
    }

    public Long getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    public Member getMember() {
        return member;
    }

    public Money getDeliveryFee() {
        return deliveryFee;
    }
}
