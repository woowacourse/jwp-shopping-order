package cart.dao;

import cart.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertProducts;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.insertProducts = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
    }

    public Product getProductById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new ProductRowMapper(), productId);
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, new ProductRowMapper());
    }

    public Long createProduct(Product product) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", product.getName());
        parameters.put("price", product.getPrice());
        parameters.put("image_url", product.getImageUrl());
        parameters.put("point_ratio", product.getPointRatio());
        parameters.put("point_available", product.getPointAvailable());

        return insertProducts.executeAndReturnKey(parameters).longValue();
    }

    public int updateProduct(Long productId, Product product) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ?, point_ratio = ?, point_available = ? WHERE id = ?";
        return jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), product.getPointRatio(), product.getPointAvailable(), productId);
    }

    public int deleteProduct(Long productId) {
        String sql = "DELETE FROM product WHERE id = ?";
        return jdbcTemplate.update(sql, productId);
    }

    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Product(
                    rs.getLong("id"),
                    rs.getString("name"),
                    (int) rs.getLong("price"),
                    rs.getString("image_url"),
                    rs.getDouble("point_ratio"),
                    rs.getBoolean("point_available")
            );
        }
    }
}
