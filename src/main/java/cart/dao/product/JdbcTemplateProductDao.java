package cart.dao.product;

import cart.domain.product.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTemplateProductDao implements ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Product> findProductById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        List<Product> products = jdbcTemplate.query(sql, new Object[]{productId}, new ProductRowMapper());
        if (products.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(products.get(0));
    }

    @Override
    public List<Product> findAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, new ProductRowMapper());
    }

    @Override
    public Long createProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url, stock) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, product.getName());
            ps.setLong(2, product.getPrice());
            ps.setString(3, product.getImageUrl());
            ps.setLong(4, product.getStock());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void updateProduct(Long productId, Product product) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ?, stock = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), product.getStock(), productId);
    }

    @Override
    public void updateStock(Long productId, Long updateStock) {
        String sql = "UPDATE product SET stock = ? WHERE id = ?";
        jdbcTemplate.update(sql, updateStock, productId);
    }

    @Override
    public void deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }

    private static class ProductRowMapper implements RowMapper<Product> {

        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Product(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("price"),
                    rs.getString("image_url"),
                    rs.getLong("stock"));
        }
    }
}
