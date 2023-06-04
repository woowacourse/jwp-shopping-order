package cart.dao;

import cart.domain.Product;
import cart.domain.vo.Amount;
import cart.exception.BusinessException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> getAllProducts() {
        final String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            final Long productId = rs.getLong("id");
            final String name = rs.getString("name");
            final int price = rs.getInt("price");
            final String imageUrl = rs.getString("image_url");
            return new Product(productId, name, Amount.of(price), imageUrl);
        });
    }

    public Product getProductById(final Long productId) {
        final String sql = "SELECT * FROM product WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
                final String name = rs.getString("name");
                final int price = rs.getInt("price");
                final String imageUrl = rs.getString("image_url");
                return new Product(productId, name, Amount.of(price), imageUrl);
            });
        } catch (final EmptyResultDataAccessException e) {
            throw new BusinessException("존재하지 않는 데이터입니다.");
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
            ps.setInt(2, product.getAmount().getValue());
            ps.setString(3, product.getImageUrl());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void updateProduct(final Long productId, final Product product) {
        final String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getAmount().getValue(), product.getImageUrl(), productId);
    }

    public void deleteProduct(final Long productId) {
        final String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
