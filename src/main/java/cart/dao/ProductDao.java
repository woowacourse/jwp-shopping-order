package cart.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cart.entity.ProductEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {

    private final RowMapper<ProductEntity> rowMapper = (rs, rowNum) ->
            new ProductEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url"),
                    rs.getInt("stock")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    public ProductEntity insert(final ProductEntity productEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", productEntity.getName());
        params.put("price", productEntity.getPrice());
        params.put("image_url", productEntity.getImageUrl());
        params.put("stock", productEntity.getStock());

        final long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new ProductEntity(id, productEntity);
    }

    public List<ProductEntity> findAll() {
        final String sql = "SELECT id, name, price, image_url, stock FROM product";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<ProductEntity> findById(final Long id) {
        final String sql = "SELECT id, name, price, image_url, stock FROM product WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<ProductEntity> update(final ProductEntity productEntity) {
        final String sql = "UPDATE product SET name = ?, price = ?, image_url = ?, stock = ? WHERE id = ?";
        jdbcTemplate.update(sql, productEntity.getName(), productEntity.getPrice(), productEntity.getImageUrl(), productEntity.getStock(), productEntity.getId());
        return findById(productEntity.getId());
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
