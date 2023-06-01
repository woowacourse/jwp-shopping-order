package cart.dao;

import cart.entity.CartItemEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<CartItemEntity> cartItemRowMapper = (rs, rowNum) ->
            new CartItemEntity(
                    rs.getLong("id"),
                    rs.getInt("quantity"),
                    rs.getLong("member_id"),
                    rs.getLong("product_id")
            );

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public CartItemEntity save(final CartItemEntity cartItemEntity) {
        final SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(cartItemEntity);
        final long savedId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        return new CartItemEntity(
                savedId,
                cartItemEntity.getQuantity(),
                cartItemEntity.getMemberId(),
                cartItemEntity.getProductId()
        );
    }

    public List<CartItemEntity> findAllByMemberId(final Long memberId) {
        final String sql = "select * from cart_item where member_id = ?";
        return jdbcTemplate.query(sql, cartItemRowMapper, memberId);
    }

    public CartItemEntity findById(final Long id) {
        final String sql = "select * from cart_item where id = ?";
        return jdbcTemplate.queryForObject(sql, cartItemRowMapper, id);
    }

    public void deleteById(final Long id) {
        final String sql = "delete from cart_item where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(final CartItemEntity cartItemEntity) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItemEntity.getQuantity(), cartItemEntity.getId());
    }
}

