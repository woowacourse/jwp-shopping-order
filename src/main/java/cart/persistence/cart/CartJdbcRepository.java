package cart.persistence.cart;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartRepository;

@Repository
public class CartJdbcRepository implements CartRepository {

	private static final RowMapper<CartItem> CART_ITEM_ROW_MAPPER = new CartItemRowMapper();

	private final JdbcTemplate jdbcTemplate;

	public CartJdbcRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Long save(Cart cart) {
		final String sql = "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)";

		final CartItem cartItem = cart.getCartItems().get(0);

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[] {"ID"});
			ps.setLong(1, cart.getMemberId());
			ps.setLong(2, cartItem.getProductId());
			ps.setInt(3, cartItem.getQuantity());
			return ps;
		}, keyHolder);

		return Objects.requireNonNull(keyHolder.getKey()).longValue();
	}

	@Override
	public Cart findByMemberId(Long memberId) {
		String sql = createBaseCartQuery("WHERE cart_item.member_id = ?");
		final List<CartItem> cartItems = jdbcTemplate.query(sql, CART_ITEM_ROW_MAPPER, memberId);
		return new Cart(memberId, cartItems);
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
