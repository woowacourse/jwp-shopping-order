package cart.dao;

import cart.dao.entity.ProductEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<ProductEntity> rowMapper = (rs, rowNum) -> new ProductEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url")
    );

    public Long save(final ProductEntity productEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("name", productEntity.getName())
                .addValue("price", productEntity.getPrice())
                .addValue("image_url", productEntity.getImageUrl())
        ).longValue();
    }

    public List<ProductEntity> findAll() {
        final String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<ProductEntity> findById(final Long id) {
        final String sql = "SELECT * FROM product WHERE id = ? ";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(final ProductEntity productEntity) {
        final String sql = "UPDATE product" +
                " SET name = ?, price = ?, image_url = ?" +
                " WHERE id = ? ";
        jdbcTemplate.update(
                sql,
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl(),
                productEntity.getId()
        );
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM product WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
