package cart.entity.cart;

public class CartEntity {

    private final Long id;
    private final Long memberId;

    public CartEntity(final Long id, final Long memberId) {
        this.id = id;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }
}
