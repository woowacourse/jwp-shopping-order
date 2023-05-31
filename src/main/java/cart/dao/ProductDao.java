package cart.dao;

import cart.entity.ProductEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<ProductEntity> productRowMapper = (rs, rowNum) ->
            new ProductEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getInt("price"),
                    rs.getString("image_url"),
                    rs.getBoolean("is_discounted"),
                    rs.getInt("discount_rate")
            );

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    public ProductEntity save(final ProductEntity productEntity) {
        final SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(productEntity);
        final long savedId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        return new ProductEntity(
                savedId,
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl(),
                productEntity.isDiscounted(),
                productEntity.getDiscountRate()
        );
    }

    public List<ProductEntity> findAll() {
        final String sql = "select * from product";
        return jdbcTemplate.query(sql, productRowMapper);
    }


    public ProductEntity findById(final Long id) {
        final String sql = "select * from product where id = ?";
        return jdbcTemplate.queryForObject(sql, productRowMapper, id);
    }

    public void updateById(final Long id, final ProductEntity productEntity) {
        final String sql = "update product set "
                + "name = ?, price = ?, image_url = ?, is_discounted = ?, discount_rate = ? "
                + "where id = ?";
        jdbcTemplate.update(
                sql,
                productRowMapper,
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getImageUrl(),
                productEntity.isDiscounted(),
                productEntity.getDiscountRate(),
                id
        );
    }

    public void deleteById(final Long id) {
        final String sql = "delete from product where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
