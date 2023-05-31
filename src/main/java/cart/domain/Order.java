package cart.domain;

import java.util.List;

public class Order {

    private Long id;
    private final Member member;
    private final List<CartItem> cartItems;
    private final Long originalPrice;
    private final Long usedPoint;
    private final Long pointToAdd;

    public Order(Member member, List<CartItem> cartItems, Long originalPrice, Long usedPoint, Long pointToAdd) {
        this.member = member;
        this.cartItems = cartItems;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public Order(Long id, Member member, List<CartItem> cartItems, Long originalPrice, Long usedPoint, Long pointToAdd) {
        this.id = id;
        this.member = member;
        this.cartItems = cartItems;
        this.originalPrice = originalPrice;
        this.usedPoint = usedPoint;
        this.pointToAdd = pointToAdd;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public Long getUsedPoint() {
        return usedPoint;
    }

    public Long getPointToAdd() {
        return pointToAdd;
    }
}
