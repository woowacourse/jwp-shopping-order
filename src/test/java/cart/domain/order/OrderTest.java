package cart.domain.order;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cart.domain.cart.CartItem;
import cart.domain.product.Product;

class OrderTest {

	private List<OrderItem> orderItems;
	private Long totalPrice;

	@BeforeEach
	public void setUp() {
		orderItems = List.of(
			new OrderItem(new CartItem(1L, new Product("NameA", 1000L, "imageUrl1"), 2)),
			new OrderItem(new CartItem(2L, new Product("NameB", 2000L, "imageUrl2"), 1)),
			new OrderItem(new CartItem(3L, new Product("NameC", 3000L, "imageUrl3"), 4)),
			new OrderItem(new CartItem(4L, new Product("NameD", 4000L, "imageUrl4"), 5))
		);
		totalPrice = 1000L * 2 + 2000L * 1 + 3000L * 4 + 4000L * 5;
	}

	@Test
	void isSameTotalPrice() {
		// given
		final Order order = new Order(null, orderItems, new DeliveryFee());

		// when
		final boolean actual = order.isSameTotalPrice(totalPrice);

		// then
		assertThat(actual).isTrue();
	}

	@Test
	void isDifferentTotalPrice() {
		// given
		final Order order = new Order(null, orderItems, new DeliveryFee());

		// when
		final boolean actual = order.isSameTotalPrice(totalPrice - 1);

		// then
		assertThat(actual).isFalse();
	}

	@Test
	void calculateTotalProductPrice() {
		// given
		final Order order = new Order(null, orderItems, new DeliveryFee());

		// when
		final Long actual = order.calculateTotalProductPrice();

		// then
		assertThat(actual).isEqualTo(totalPrice);
	}

	@Test
	void calculateTotalOrderPrice() {
		// given
		final DeliveryFee deliveryFee = new DeliveryFee();
		final Order order = new Order(null, orderItems, deliveryFee);

		// when
		final Long actual = order.calculateTotalOrderPrice();

		// then
		assertThat(actual).isEqualTo(totalPrice + deliveryFee.getDeliveryFee());
	}
}
