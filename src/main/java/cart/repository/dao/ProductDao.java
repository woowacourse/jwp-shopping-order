package cart.repository.dao;

import cart.entity.ProductEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    public Page<ProductEntity> getProducts(Pageable pageable) {
        final int pageSize = pageable.getPageSize();
        final long offset = pageable.getOffset();

        String sql = "SELECT * FROM product "
                + "WHERE is_deleted = FALSE "
                + "LIMIT ? "
                + "OFFSET ? ";
        final List<ProductEntity> products = jdbcTemplate.query(sql, new Object[]{pageSize, offset}, productRowMapper);

        String countSql = "SELECT COUNT(*) FROM product";
        final Integer totalProductCount = jdbcTemplate.queryForObject(countSql, Integer.class);

        return new PageImpl<>(products, pageable, totalProductCount);
    }

    public ProductEntity getProductById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = ? AND is_deleted = FALSE";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, productRowMapper);
    }

    public List<ProductEntity> getProductByIds(List<Long> productIds) {
        String sql = "SELECT id, name, price, image_url, is_deleted FROM product WHERE id IN (:ids) AND is_deleted = FALSE";

        Map<String, Object> params = Collections.singletonMap("ids", productIds);

        return namedParameterJdbcTemplate.query(sql, params, productRowMapper);
    }

    public Long createProduct(ProductEntity product) {
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

    public void updateProduct(Long productId, ProductEntity product) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getImageUrl(), productId);
    }

    public void deleteProduct(Long productId) {
        String sql = "UPDATE product SET is_deleted = TRUE WHERE id = ?";
        jdbcTemplate.update(sql, productId);
    }

    private final RowMapper<ProductEntity> productRowMapper = (rs, rowNum) -> {
        Long productId = rs.getLong("id");
        String name = rs.getString("name");
        int price = rs.getInt("price");
        String imageUrl = rs.getString("image_url");
        boolean isDeleted = rs.getBoolean("is_deleted");
        return new ProductEntity(productId, name, price, imageUrl, isDeleted);
    };
}
