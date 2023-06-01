package cart.dao;

import cart.entity.ProductEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    public List<ProductEntity> getAll() {
        String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, new ProductMapper());
    }

    public Optional<ProductEntity> getById(Long productId) {
        String sql = "SELECT * FROM product WHERE id = ?";
        try {
            ProductEntity product = jdbcTemplate.queryForObject(sql, new ProductMapper(), productId);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Long insert(ProductEntity entity) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(entity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    public void update(Long id, String name, Integer price, String imageUrl) {
        String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, name, price, imageUrl, id);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class ProductMapper implements RowMapper<ProductEntity> {
        @Override
        public ProductEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ProductEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url")
            );
        }
    }
}
