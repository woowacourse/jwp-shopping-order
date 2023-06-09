package cart.domain.order;

import java.math.BigDecimal;

import cart.domain.cart.CartItem;
import cart.domain.product.Product;

public class OrderItem {

	private final Long id;
	private final Product product;
	private final int quantity;

	public OrderItem(final CartItem cartItem) {
		this(null, cartItem.getProduct(), cartItem.getQuantity());
	}

	public OrderItem(final Long id, final Product product, final int quantity) {
		this.id = id;
		this.product = product;
		this.quantity = quantity;
	}

	public BigDecimal calculatePrice() {
		return product.getPrice().multiply(BigDecimal.valueOf(quantity));
	}

	public Long getId() {
		return id;
	}

	public Product getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}
}
