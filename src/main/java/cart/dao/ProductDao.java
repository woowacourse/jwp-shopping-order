package cart.dao;

import cart.entity.ProductEntity;
import cart.exception.ProductNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static cart.entity.RowMapperUtil.productEntityRowMapper;

@Repository
public class ProductDao {

    private static final int SINGLE_AFFECTED_ROW_NUMBER = 1;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public ProductDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    public List<ProductEntity> findAll() {
        final String sql = "SELECT * FROM product";
        return jdbcTemplate.query(sql, productEntityRowMapper);
    }

    public ProductEntity findById(final long id) {
        final String sql = "SELECT * FROM product WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, productEntityRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ProductNotFoundException();
        }
    }

    public long insert(final ProductEntity source) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(source);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    // TODO: 업데이트 사항이 없는 경우에 다른 예외로 처리 필요.
    public void update(final ProductEntity productEntity) {
        final String sql = "UPDATE product SET name = ?, price = ?, image_url = ? WHERE id = ?";
        final int affectedRowNumber = jdbcTemplate.update(sql,
                productEntity.getName(), productEntity.getPrice(),
                productEntity.getImageUrl(), productEntity.getId());
        validateSingleAffectedRow(affectedRowNumber);
    }

    public void deleteById(final long id) {
        String sql = "DELETE FROM product WHERE id = ?";
        final int affectedRowNumber = jdbcTemplate.update(sql, id);
        validateSingleAffectedRow(affectedRowNumber);
    }

    private void validateSingleAffectedRow(final int affectedRowNumber) {
        if (affectedRowNumber != SINGLE_AFFECTED_ROW_NUMBER) {
            throw new ProductNotFoundException();
        }
    }
}
