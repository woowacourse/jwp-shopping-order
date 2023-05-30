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
		Long productId = rs.getLong("id");
		String name = rs.getString("name");
		int price = rs.getInt("price");
		String imageUrl = rs.getString("image_url");
		return new Product(productId, name, price, imageUrl);
	}

	private CartItem mapCartItem(ResultSet rs, Product product) throws SQLException {
		Long cartItemId = rs.getLong("cart_item.id");
		int quantity = rs.getInt("cart_item.quantity");
		return new CartItem(cartItemId, product, quantity);
	}
}
