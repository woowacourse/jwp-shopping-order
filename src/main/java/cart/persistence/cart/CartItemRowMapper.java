package cart.persistence.cart;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cart.domain.cart.CartItem;
import cart.domain.product.Product;

public class CartItemRowMapper implements RowMapper<CartItem> {

	@Override
	public CartItem mapRow(ResultSet rs, int rowNum) throws SQLException {
		Product product = mapProduct(rs);
		return mapCartItem(rs, product);
	}

	private Product mapProduct(ResultSet rs) throws SQLException {
		return new Product(
			rs.getLong("product.id"),
			rs.getString("name"),
			rs.getBigDecimal("price"),
			rs.getString("image_url")
		);
	}

	private CartItem mapCartItem(ResultSet rs, Product product) throws SQLException {
		return new CartItem(
			rs.getLong("cart_item.id"),
			product,
			rs.getInt("cart_item.quantity")
		);
	}
}
