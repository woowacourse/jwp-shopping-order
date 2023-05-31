package cart.dao;

import cart.domain.CartItemEntity;
import cart.exception.CartItemNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class CartItemDao {

    private static final String TABLE = "cart_item";
    private static final String ID = "id";
    private static final String MEMBER_ID = "member_id";
    private static final String PRODUCT_ID = "product_id";
    private static final String QUANTITY = "quantity";
    private static final String ALL_COLUMN = String.join(", ", ID, MEMBER_ID, PRODUCT_ID, QUANTITY);

    private static final RowMapper<CartItemEntity> rowMapper = (resultSet, rowNum) ->
            new CartItemEntity(
                    resultSet.getLong(ID),
                    resultSet.getLong(MEMBER_ID),
                    resultSet.getLong(PRODUCT_ID),
                    resultSet.getInt(QUANTITY)
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    public long insert(final CartItemEntity cartItemEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(cartItemEntity);

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public CartItemEntity findById(final Long id) {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + " WHERE id = ?;";

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (final EmptyResultDataAccessException e) {
            throw new CartItemNotFoundException();
        }
    }

    public List<CartItemEntity> findAll() {
        String sql = "SELECT " + ALL_COLUMN + " FROM " + TABLE + ";";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public void update(final CartItemEntity cartItemEntity) {
        String sql = "UPDATE " + TABLE + " SET member_id = ?, product_id = ?, quantity = ? WHERE id = ?;";

        jdbcTemplate.update(sql, cartItemEntity.getMemberId(), cartItemEntity.getProductId(), cartItemEntity.getQuantity(), cartItemEntity.getId());
    }

    public void deleteById(final long id) {
        String sql = "DELETE FROM " + TABLE + " WHERE id = ?;";

        jdbcTemplate.update(sql, id);
    }
}

