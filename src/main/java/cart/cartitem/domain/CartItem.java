package cart.cartitem.domain;

import static cart.cartitem.exception.CartItemExceptionType.NO_AUTHORITY_UPDATE_ITEM;

import cart.cartitem.exception.CartItemException;
import cart.member.domain.Member;
import cart.product.domain.Product;
import java.util.Objects;

public class CartItem {

    private final Long id;
    private int quantity;
    private final Long productId;
    private final String name;
    private final String imageUrl;
    private final int productPrice;
    private final Long memberId;

    public CartItem(Product product, Member member) {
        this(null, 1, product, member);
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        this(id,
                quantity,
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                member.getId());
    }

    public CartItem(
            Long id,
            int quantity,
            Long productId,
            String name,
            String imageUrl,
            int productPrice,
            Long memberId
    ) {
        this.id = id;
        this.quantity = quantity;
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.productPrice = productPrice;
        this.memberId = memberId;
    }

    public void checkOwner(Member member) {
        if (!Objects.equals(this.memberId, member.getId())) {
            throw new CartItemException(NO_AUTHORITY_UPDATE_ITEM);
        }
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getMemberId() {
        return memberId;
    }
}
