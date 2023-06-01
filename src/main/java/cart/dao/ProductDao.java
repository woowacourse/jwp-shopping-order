package cart.dao;

import cart.domain.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductDao {

    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Long productId = rs.getLong("id");
        String name = rs.getString("name");
        int price = rs.getInt("price");
        String imageUrl = rs.getString("image_url");
        return new Product(productId, name, price, imageUrl);
    };

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> getAllProducts() {
        final String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    public Optional<Product> getProductById(final Long productId) {
        try {
            final String sql = "SELECT * FROM product WHERE id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
                final String name = rs.getString("name");
                final int price = rs.getInt("price");
                final String imageUrl = rs.getString("image_url");
                return new Product(productId, name, price, imageUrl);
            }));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long createProduct(final Product product) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, product.getName());
            ps.setInt(2, product.getPrice());
            ps.setString(3, product.getImageUrl());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public int updateProduct(final Long productId, final Product product) {
        final String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        return jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), productId);
    }

    public int deleteProduct(final Long productId) {
        final String sql = "DELETE FROM product WHERE id = ?";
        return jdbcTemplate.update(sql, productId);
    }

    public List<Product> getProductByIds(final List<Long> productIds) {
        final StringBuilder sql = new StringBuilder("SELECT * FROM product WHERE id IN ");
        sql.append(productIds.stream().map(String::valueOf)
                .collect(Collectors.joining(", ", "(", ")")));
        return jdbcTemplate.query(sql.toString(), productRowMapper);
    }
}
