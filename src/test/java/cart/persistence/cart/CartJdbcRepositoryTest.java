package cart.persistence.cart;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartRepository;
import cart.domain.product.Product;
import cart.persistence.product.ProductJdbcRepository;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CartJdbcRepositoryTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private CartRepository cartRepository;

	@BeforeEach
	void setUp() {
		cartRepository = new CartJdbcRepository(jdbcTemplate);
	}

	@Test
	void save() {
		// given
		final long memberId = 1L;
		final Product product = new ProductJdbcRepository(jdbcTemplate).findById(1L).get();
		final CartItem cartItem = new CartItem(product);
		final Cart cart = new Cart(memberId, List.of(cartItem));

		// when
		final Long savedId = cartRepository.save(cart);

		//then
		final CartItem actualItem = cartRepository.findByMemberId(memberId)
			.getCartItem(savedId)
			.get();

		assertThat(actualItem.getProduct().getName()).isEqualTo(product.getName());
		assertThat(actualItem.getProduct().getPrice()).isEqualTo(product.getPrice());
		assertThat(actualItem.getProduct().getImageUrl()).isEqualTo(product.getImageUrl());
		assertThat(actualItem.getQuantity()).isEqualTo(cartItem.getQuantity());

	}

	@Test
	void findByMemberId() {
		// given
		final long memberId = 1L;

		// when
		final Cart cart = cartRepository.findByMemberId(memberId);

		// then
		final List<Long> expected = List.of(1L, 2L, 3L, 4L, 5L, 6L);
		final List<Long> actual = cart.getCartItems().stream()
			.map(CartItem::getId)
			.collect(Collectors.toList());

		assertThat(actual).isEqualTo(expected);
	}
}
