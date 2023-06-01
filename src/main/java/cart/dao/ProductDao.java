package cart.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cart.domain.Product;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Product> rowMapper = (rs, rowNum) -> {
        Long productId = rs.getLong("id");
        String name = rs.getString("name");
        int price = rs.getInt("price");
        String imageUrl = rs.getString("image_url");
        return new Product(productId, name, price, imageUrl);
    };

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Product getProductById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, productId);
    }

    public Long createProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
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

    public void updateProduct(Long productId, Product product) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), productId);
    }

    public void deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }

    public List<Product> findByIds(List<Long> ids) {
        String inClause = String.join(",", Collections.nCopies(ids.size(), "?"));
        final String sql = "SELECT * FROM product WHERE id IN (" + inClause + ")";
        return jdbcTemplate.query(sql, rowMapper, ids.toArray());
    }
}
