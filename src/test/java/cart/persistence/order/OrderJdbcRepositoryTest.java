package cart.persistence.order;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.order.DeliveryFee;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.product.Product;
import cart.error.exception.OrderException;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
class OrderJdbcRepositoryTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private OrderJdbcRepository orderJdbcRepository;

	@BeforeEach
	public void setUp() {
		orderJdbcRepository = new OrderJdbcRepository(jdbcTemplate);
	}

	@Test
	void save() {
		// given
		final String imageUrl = "imageUrl";
		final String itemName = "itemName";
		final Long price = 1000L;
		final int quantity = 4;

		final OrderItem item1 = new OrderItem(null, new Product(13L, itemName, price, imageUrl), quantity);
		final OrderItem item2 = new OrderItem(null, new Product(14L, "itemName2", 2000L, "imageUrl2"), 5);

		final Order expected = new Order(null, List.of(item1, item2), new DeliveryFee(3000L));

		// when
		final Long savedId = orderJdbcRepository.save(1L, expected);

		// then
		final Order actual = orderJdbcRepository.findById(savedId);
		final Product actualProduct = actual.getOrderItems().get(0).getProduct();
		final Product expectedProduct = expected.getOrderItems().get(0).getProduct();

		assertThat(actual.getOrderItems().size()).isEqualTo(expected.getOrderItems().size());
		assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName());
		assertThat(actual.calculateTotalOrderPrice()).isEqualTo(expected.calculateTotalOrderPrice());
	}

	@Test
	void findByMemberId() {
		//given
		final long memberId = 1L;

		//when
		final List<Order> orders = orderJdbcRepository.findByMemberId(memberId);

		//then
		assertThat(orders.size()).isEqualTo(3);
	}

	@Test
	void findById() {
		// given
		final Long orderId = 1L;

		// when
		final Order order = orderJdbcRepository.findById(orderId);

		// then
		assertThat(order.getId()).isEqualTo(orderId);
	}

	@Test
	void deleteById() {
		//given
		final long orderId = 1L;

		// when
		orderJdbcRepository.deleteById(orderId);

		assertThatThrownBy(() -> orderJdbcRepository.findById(orderId))
			.isInstanceOf(OrderException.NotFound.class)
			.hasMessage("해당 주문이 존재하지 않습니다.");
	}
}
