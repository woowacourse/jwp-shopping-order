package cart.entity;

public class CartItemEntity {

    private ProductEntity productEntity;
    private MemberEntity memberEntity;
    private final Long id;
    private final int quantity;

    public CartItemEntity(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public CartItemEntity(ProductEntity productEntity, MemberEntity memberEntity, Long id, int quantity) {
        this.productEntity = productEntity;
        this.memberEntity = memberEntity;
        this.id = id;
        this.quantity = quantity;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }
}
