package cart.domain.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import cart.domain.cart.CartItem;
import cart.domain.product.Product;

class OrderItemTest {

	@Test
	void calculatePriceTest() {
		// given
		final int quantity = 2;
		final long price = 5000L;
		Product product = new Product(1L, "Product", price, "ImageUrl");
		final OrderItem orderItem = new OrderItem(new CartItem(null, product, quantity));

		// when
		final Long calculatePrice = orderItem.calculatePrice();

		// then
		Assertions.assertThat(calculatePrice).isEqualTo(price * quantity);

	}
}
