package cart.infrastructure.dao;

import cart.domain.Product;
import cart.entity.ProductEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<ProductEntity> rowMapper = (rs, rowNum) ->
            ProductEntity.of(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url")
            );

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    public Long create(final ProductEntity product) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", product.getName());
        params.put("price", product.getPrice());
        params.put("image_url", product.getImageUrl());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<ProductEntity> findAll() {
        final String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<ProductEntity> findById(final Long id) {
        final String sql = "SELECT * FROM product WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(final Long id, final Product product) {
        final String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        jdbcTemplate.update(sql, product.getName(), product.getAmount().getValue(), product.getImageUrl(), id);
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
