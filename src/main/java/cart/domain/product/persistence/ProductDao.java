package cart.domain.product.persistence;

import java.util.List;

import cart.domain.product.domain.Product;
import cart.global.exception.ProductNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Product> rowMapper = (rs, rowNum) -> {
        long productId = rs.getLong("id");
        String name = rs.getString("name");
        int price = rs.getInt("price");
        String imageUrl = rs.getString("image_url");
        return new Product(productId, name, price, imageUrl);
    };

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product").usingGeneratedKeyColumns("id");
    }

    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Product> selectFirstProductsByLimit(int limit) {
        String sql = "SELECT * FROM product ORDER BY id DESC LIMIT ?";
        return jdbcTemplate.query(sql, rowMapper, limit);
    }

    public List<Product> selectProductsByIdAndLimit(Long lastId, int limit) {
        String sql = "SELECT * FROM product WHERE id < ? ORDER BY id DESC LIMIT ?";
        return jdbcTemplate.query(sql, rowMapper, lastId, limit);
    }

    public Product selectLastProduct() {
        String sql = "SELECT * FROM product ORDER BY id ASC LIMIT 1";
        return jdbcTemplate.query(sql, rowMapper).stream()
                .findAny()
                .orElseThrow(() -> new ProductNotFoundException("최근 상품이 존재하지 않습니다."));
    }

    public Product getProductById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, productId);
    }

    public Long insertProduct(Product product) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", product.getName())
                .addValue("price", product.getPrice())
                .addValue("image_url", product.getImageUrl());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Boolean isNotExistById(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM product WHERE id = ?)";
        return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
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
