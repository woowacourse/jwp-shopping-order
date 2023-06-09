package cart.persistence.product;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cart.domain.product.Product;

public class ProductRowMapper implements RowMapper<Product> {
	@Override
	public Product mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		return new Product(
			rs.getLong("id"),
			rs.getString("name"),
			rs.getBigDecimal("price"),
			rs.getString("image_url")
		);
	}
}
