package cart.cartitem.domain;

import static cart.cartitem.exception.CartItemExceptionType.NO_AUTHORITY_UPDATE_ITEM;

import cart.cartitem.exception.CartItemException;
import cart.member.domain.Member;
import cart.product.domain.Product;
import java.util.Objects;

public class CartItem {

    private final Long id;
    private int quantity;
    private final CartProduct cartProduct;
    private final Long productId;
    private final Long memberId;

    public CartItem(Product product, Member member) {
        this(null, 1, product, member.getId());
    }

    public CartItem(Long id, int quantity, Product product, Member member) {
        this(id, quantity, product, member.getId());
    }

    public CartItem(Long id, int quantity, Product product, Long memberId) {
        this.id = id;
        this.quantity = quantity;
        this.cartProduct = CartProduct.from(product);
        this.productId = product.getId();
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

    public boolean matches(Product product) {
        return Objects.equals(cartProduct, CartProduct.from(product));
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return cartProduct.getName();
    }

    public String getImageUrl() {
        return cartProduct.getImageUrl();
    }

    public int getPrice() {
        return cartProduct.getPrice();
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
