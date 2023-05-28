package cart.domain;

import java.util.Objects;

public class CartItem {

    private Long id;
    private int quantity;
    private final Product product;
    private final Long memberId;

    public CartItem(Product product, Long memberId) {
        this.quantity = 1;
        this.product = product;
        this.memberId = memberId;
    }

    public CartItem(Long id, int quantity, Product product, Long memberId) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void checkOwner(Long memberId) {
        if (!Objects.equals(memberId, this.memberId)) {
            throw new IllegalArgumentException();
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
