package cart.domain.order;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cart.domain.cart.CartItem;
import cart.domain.coupon.type.NotUsed;
import cart.domain.monetary.DeliveryFee;
import cart.domain.product.Product;

class OrdersTest {

	private List<Order> orders;

	@BeforeEach
	public void setUp() {
		orders = List.of(
			new Order(1L, List.of(new OrderItem(new CartItem(1L, new Product("NameA", BigDecimal.valueOf(1000L), "imageUrl1"), 2))),
				new NotUsed(), new DeliveryFee(), OrderStatus.PAID, LocalDateTime.now().minusHours(3)),
			new Order(2L, List.of(new OrderItem(new CartItem(2L, new Product("NameB", BigDecimal.valueOf(2000L), "imageUrl2"), 1))),
				new NotUsed(), new DeliveryFee(), OrderStatus.PAID, LocalDateTime.now().minusHours(2)),
			new Order(3L, List.of(new OrderItem(new CartItem(3L, new Product("NameC", BigDecimal.valueOf(3000L), "imageUrl3"), 4))),
				new NotUsed(), new DeliveryFee(), OrderStatus.PAID, LocalDateTime.now().minusHours(1)),
			new Order(4L, List.of(new OrderItem(new CartItem(4L, new Product("NameD", BigDecimal.valueOf(4000L), "imageUrl4"), 5))),
				new NotUsed(), new DeliveryFee(), OrderStatus.PAID, LocalDateTime.now())
		);
	}

	@Test
	void contain() {
		// given
		final Orders orders = new Orders(1L, this.orders);

		// when
		final boolean actual = orders.contain(3L);

		// then
		assertThat(actual).isTrue();
	}

	@Test
	void getOrder() {
		// given
		final Orders orders = new Orders(1L, this.orders);

		// when
		final Order actual = orders.getOrder(1L).get();

		// then
		assertThat(actual).isEqualTo(this.orders.get(0));

	}

	@Test
	void getOrders() {
		// given
		final Orders orders = new Orders(1L, this.orders);

		// when
		final List<Order> actual = orders.getOrders();

		//then
		final List<Order> expected = new ArrayList<>(this.orders);
		Collections.reverse(expected);
		assertThat(actual).isEqualTo(expected);
	}
}
