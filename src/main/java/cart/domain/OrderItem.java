package cart.domain;

public class OrderItem extends Item {

    public OrderItem(Long id, int quantity, Product product, Member member) {
        super(id, quantity, product, member);
    }
}
