package cart.application.cart.dto;

import cart.application.product.dto.ProductDto;
import cart.domain.cart.CartItem;
import cart.ui.cart.dto.CartItemRequest;

public class CartItemDto {

	private final Long id;
	private final int quantity;
	private final ProductDto product;

	public CartItemDto(final CartItemRequest cartItemRequest) {
		this(null, 1, new ProductDto(cartItemRequest.getProductId(), null, 0L, null));

	}

	public CartItemDto(final Long id, final int quantity, final ProductDto product) {
		this.id = id;
		this.quantity = quantity;
		this.product = product;
	}

	public static CartItemDto from(CartItem cartItem) {
		return new CartItemDto(
			cartItem.getId(),
			cartItem.getQuantity(),
			ProductDto.from(cartItem.getProduct())
		);
	}

	public Long getId() {
		return id;
	}

	public int getQuantity() {
		return quantity;
	}

	public Long getProductId() {
		return product.getId();
	}

	public ProductDto getProduct() {
		return product;
	}
}
