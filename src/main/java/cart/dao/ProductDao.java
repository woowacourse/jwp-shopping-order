package cart.dao;

import cart.domain.Price;
import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        Price price = new Price(rs.getInt("price"));
        String imageUrl = rs.getString("image_url");
        return new Product(id, name, price, imageUrl);
    };

    public ProductDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("product")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "price", "image_url");
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT id, name, price, image_url FROM product";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    public Optional<Product> getProductById(Long productId) {
        String sql = "SELECT id, name, price, image_url FROM product WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, productRowMapper, productId));
    }

    public Long createProduct(Product product) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", product.getName());
        params.put("price", product.getPrice().getValue());
        params.put("image_url", product.getImageUrl());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public void updateProduct(Long productId, Product product) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), productId);
    }

    public void deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
