package cart.domain;

public class CartItem extends Item {

    public CartItem(final Member member, final Product product) {
        super(null, 1, product, member);
    }

    public CartItem(final Long id, final int quantity, final Product product, final Member member) {
        super(id, quantity, product, member);
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
