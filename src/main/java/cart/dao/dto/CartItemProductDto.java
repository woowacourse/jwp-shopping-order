package cart.dao.dto;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Product;
import cart.domain.Quantity;

public class CartItemProductDto {

    private final long cartItemId;
    private final long productId;
    private final long memberId;
    private final String email;
    private final int quantity;
    private final String productName;
    private final int price;
    private final String productImageUrl;

    public CartItemProductDto(long cartItemId, long productId, long memberId, String email, int quantity, String productName,
        int price, String productImageUrl) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.memberId = memberId;
        this.email = email;
        this.quantity = quantity;
        this.productName = productName;
        this.price = price;
        this.productImageUrl = productImageUrl;
    }

    public CartItem toDomain() {
        return new CartItem(
            cartItemId,
            Quantity.from(quantity),
            new Product(productId, productName, Money.from(price), productImageUrl),
            new Member(memberId, email, null)
        );
    }
}
