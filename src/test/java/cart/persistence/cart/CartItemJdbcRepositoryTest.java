package cart.persistence.cart;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.cart.CartItem;
import cart.domain.cart.CartItemRepository;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
class CartItemJdbcRepositoryTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private CartItemRepository cartItemRepository;

	@BeforeEach
	void setUp() {
		cartItemRepository = new CartItemJdbcRepository(jdbcTemplate);
	}

	@Test
	void updateQuantity() {
		// given
		final CartItem cartItem = cartItemRepository.findById(1L).get();
		final int quantity = cartItem.getQuantity();
		cartItem.changeQuantity(quantity + 1);

		// when
		cartItemRepository.updateQuantity(cartItem);

		// then
		final CartItem actual = cartItemRepository.findById(1L).get();
		assertThat(actual.getQuantity()).isEqualTo(quantity + 1);
	}

	@Test
	void deleteByIds() {
		//given
		final List<Long> deleteIds = List.of(1L, 2L, 7L);
		final long memberId = 1L;

		//when
		cartItemRepository.deleteByIds(deleteIds);

		//then
		final Optional<CartItem> cartItem1 = cartItemRepository.findById(1L);
		final Optional<CartItem> cartItem2 = cartItemRepository.findById(2L);
		final Optional<CartItem> cartItem3 = cartItemRepository.findById(7L);

		assertThat(cartItem1.isEmpty()).isTrue();
		assertThat(cartItem2.isEmpty()).isTrue();
		assertThat(cartItem3.isEmpty()).isTrue();
	}
}
