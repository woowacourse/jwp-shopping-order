package cart.dao.entity;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;

public class CartItemDto {

    private final Long memberId;
    private final String email;
    private final Long productId;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final Long cartItemId;
    private final int cartItemQuantity;

    public CartItemDto(final Long memberId, final String email, final Long productId, final String name, final int price, final String imageUrl, final Long cartItemId, final int cartItemQuantity) {
        this.memberId = memberId;
        this.email = email;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.cartItemId = cartItemId;
        this.cartItemQuantity = cartItemQuantity;
    }

    public CartItem toDomain() {
        final Member member = new Member(memberId, email, null);
        final Product product = new Product(productId, name, price, imageUrl);
        return new CartItem(cartItemId, cartItemQuantity, product, member);
    }

    public String getEmail() {
        return email;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getCartItemQuantity() {
        return cartItemQuantity;
    }
}
