package cart.domain;

public class OrderItem extends Item {

    public OrderItem(Member member, Product product) {
        this(null, 1, product, member);
    }

    public OrderItem(Long id, int quantity, Product product, Member member) {
        super(id, quantity, product, member);
    }
}
