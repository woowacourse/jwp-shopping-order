package cart.persistence.cart;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cart.domain.cart.CartItem;
import cart.domain.cart.CartItemRepository;

@Repository
public class CartItemJdbcRepository implements CartItemRepository {

	private static final RowMapper<CartItem> CART_ITEM_ROW_MAPPER = new CartItemRowMapper();

	private final JdbcTemplate jdbcTemplate;

	public CartItemJdbcRepository(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Optional<CartItem> findById(final Long id) {
		final String sql = createBaseCartQuery("WHERE cart_item.id = ?");
		try {
			final CartItem cartItem = jdbcTemplate.queryForObject(sql, CART_ITEM_ROW_MAPPER, id);
			return Optional.ofNullable(cartItem);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public void updateQuantity(final CartItem cartItem) {
		final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
		jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
	}

	@Override
	public void delete(final Long memberId, final Long productId) {
		final String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
		jdbcTemplate.update(sql, memberId, productId);
	}

	@Override
	public void deleteByIds(final List<Long> ids) {
		final String sql = "DELETE FROM cart_item WHERE id = ?";

		final List<Object[]> batchArgs = ids.stream()
			.map(id -> new Object[] {id})
			.collect(Collectors.toList());

		jdbcTemplate.batchUpdate(sql, batchArgs);
	}

	private String createBaseCartQuery(final String condition) {
		final String sql =
			"SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity "
				+ "FROM cart_item "
				+ "INNER JOIN member ON cart_item.member_id = member.id "
				+ "INNER JOIN product ON cart_item.product_id = product.id "
				+ "%s";

		return String.format(sql, condition);
	}
}
