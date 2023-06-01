package cart.dao;

import cart.entity.ProductEntity;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<ProductEntity> rowMapper = (resultSet, rowNum) -> new ProductEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("image_url"),
            resultSet.getLong("price")
    );

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingColumns("name", "image_url", "price")
                .usingGeneratedKeyColumns("id");
    }

    public ProductEntity insert(final ProductEntity product) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(product);
        final long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new ProductEntity(id, product.getName(), product.getImageUrl(), product.getPrice());
    }

    public int update(final ProductEntity product) {
        final String sql = "UPDATE product SET name = ?, image_url = ?, price = ? WHERE id = ?";
        return jdbcTemplate.update(
                sql,
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                product.getId()
        );
    }

    public int deleteById(final Long id) {
        final String sql = "DELETE FROM product WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Transactional(readOnly = true)
    public List<ProductEntity> findAll() {
        final String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Transactional(readOnly = true)
    public Optional<ProductEntity> findById(final Long id) {
        final String sql = "SELECT * FROM product WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    public List<ProductEntity> findByIds(final List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        final String sql = "SELECT * FROM product WHERE id IN (:ids)";
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        return namedJdbcTemplate.query(sql, parameters, rowMapper);
    }
}
