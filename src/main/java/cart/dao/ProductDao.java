package cart.dao;

import cart.domain.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            long price = rs.getLong("price");
            String imageUrl = rs.getString("image_url");
            return new Product(productId, name, price, imageUrl);
        });
    }

    public Optional<Product> getProductById(Long productId) {
        try{
            String sql = "SELECT * FROM product WHERE id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
                String name = rs.getString("name");
                long price = rs.getLong("price");
                String imageUrl = rs.getString("image_url");
                return new Product(productId, name, price, imageUrl);
            }));
        }catch(EmptyResultDataAccessException exception){
            return Optional.empty();
        }
    }

    public Long createProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, product.getName());
            ps.setLong(2, product.getPrice());
            ps.setString(3, product.getImageUrl());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public int updateProduct(Long productId, Product product) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        return jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), productId);
    }

    public int deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        return jdbcTemplate.update(sql, productId);
    }

    public int countByProduct(final Product product) {
        String sql = "SELECT COUNT(*) AS count " +
                "FROM product " +
                "WHERE name = ? AND price = ? AND image_url = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, product.getName(), product.getPrice(), product.getImageUrl());
    }
}
