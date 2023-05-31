package cart.persistence.product;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cart.domain.product.Product;
import cart.domain.product.ProductRepository;

@Repository
public class ProductJdbcRepository implements ProductRepository {

	private static final RowMapper<Product> PRODUCT_ROW_MAPPER = new ProductRowMapper();

	private final JdbcTemplate jdbcTemplate;

	public ProductJdbcRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Long save(Product product) {
		final String sql = "INSERT INTO product (name, price, image_url) VALUES (?, ?, ?)";

		final KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, product.getName());
			ps.setLong(2, product.getPrice());
			ps.setString(3, product.getImageUrl());
			return ps;
		}, keyHolder);

		return Objects.requireNonNull(keyHolder.getKey()).longValue();
	}

	@Override
	public List<Product> findAll() {
		String sql = "SELECT * FROM product";
		return jdbcTemplate.query(sql, PRODUCT_ROW_MAPPER);
	}

	@Override
	public Optional<Product> findById(Long id) {
		String sql = "SELECT * FROM product WHERE id = ?";
		try {
			final Product product = jdbcTemplate.queryForObject(sql, PRODUCT_ROW_MAPPER, id);
			return Optional.ofNullable(product);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	@Override
	public void update(Product product) {
		String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
		jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), product.getId());
	}

	@Override
	public void deleteById(Long id) {
		String sql = "DELETE FROM product WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}
}
