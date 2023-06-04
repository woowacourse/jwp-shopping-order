package cart.entity;

public class CartItemEntity {
    private final Long id;
    private final Long member_id;
    private final Long product_id;
    private final int quantity;

    public CartItemEntity(Long id, Long member_id, Long product_id, int quantity) {
        this.id = id;
        this.member_id = member_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMember_id() {
        return member_id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public int getQuantity() {
        return quantity;
    }
}
