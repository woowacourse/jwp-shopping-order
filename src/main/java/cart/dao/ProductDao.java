package cart.dao;

import cart.dao.dto.PageInfo;
import cart.domain.Product;
import cart.entity.ProductEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ProductEntity> getAllProducts() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            return new ProductEntity(productId, name, price, imageUrl);
        });
    }

    public ProductEntity getProductById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, productEntityRowMapper(), productId);
    }

    public RowMapper<ProductEntity> productEntityRowMapper() {
        return (rs, rowNum) -> new ProductEntity(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getString("image_url")
        );
    }

    public Long createProduct(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO product (name, price, image_url) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

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

    public List<ProductEntity> findProductsByPage(int size, int page) {
        String sql = "SELECT * FROM product ORDER BY id DESC LIMIT ? OFFSET ? ";
        int offset = (page - 1) * size;
        return jdbcTemplate.query(sql, productEntityRowMapper(), size, offset);
    }

    public PageInfo findPageInfo(int size, int page) {
        String sql = "SELECT COUNT(*) as total, ? as perPage, ? as currentPage, CEILING(COUNT(*) / CAST(? AS DECIMAL(10, 2))) as lastPage FROM product;";
        return jdbcTemplate.queryForObject(sql, pageInfoRowMapper(), size, page, size);
    }

    private RowMapper<PageInfo> pageInfoRowMapper() {
        return (rs, rowNum) -> new PageInfo(
                rs.getInt("total"),
                rs.getInt("perPage"),
                rs.getInt("currentPage"),
                rs.getInt("lastPage"));
    }
}
