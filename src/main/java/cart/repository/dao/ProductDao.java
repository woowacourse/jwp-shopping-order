package cart.repository.dao;

import cart.domain.Product;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            boolean isDeleted = rs.getBoolean("is_deleted");
            return new Product(productId, name, price, imageUrl, isDeleted);
        });
    }

    public Product getProductById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, (rs, rowNum) -> {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            boolean isDeleted = rs.getBoolean("is_deleted");
            return new Product(productId, name, price, imageUrl, isDeleted);
        });
    }

    public List<Product> getProductByIds(List<Long> productIds) {
        String sql = "SELECT id, name, price, image_url, is_deleted FROM product WHERE id IN (:ids)";

        Map<String, Object> params = Collections.singletonMap("ids", productIds);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            final Long id = rs.getLong("id");
            final String name = rs.getString("name");
            final int price = rs.getInt("price");
            final String imageUrl = rs.getString("image_url");
            return new Product(id, name, price, imageUrl);
        });
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
        String sql = "UPDATE product SET is_deleted = TRUE WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }

    public List<Product> getPagedProductsDescending(final int unitSize, final int page) {
        String sql = "SELECT id, name, price, image_url, is_deleted FROM product "
                + "WHERE is_deleted = FALSE "
                + "ORDER BY id DESC "
                + "LIMIT ?,?";

        int startOrder = unitSize * (page - 1);

        return jdbcTemplate.query(sql, new Object[]{startOrder, unitSize}, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            boolean isDeleted = rs.getBoolean("is_deleted");
            return new Product(id, name, price, imageUrl, isDeleted);
        });
    }
}
