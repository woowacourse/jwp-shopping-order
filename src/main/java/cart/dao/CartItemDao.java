package cart.dao;

import cart.entity.CartItemEntity;
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

@Repository
public class CartItemDao {

    private final static RowMapper<CartItemEntity> cartItemRowMapper = (rs, rowNum) ->
            new CartItemEntity(
                    rs.getLong("id"),
                    rs.getLong("product_id"),
                    rs.getLong("member_id"),
                    rs.getInt("quantity")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public List<CartItemEntity> findByMemberId(Long memberId) {
        String sql = "select * from cart_item where member_id = ?";
        return jdbcTemplate.query(sql, cartItemRowMapper, memberId);
    }

    public Long save(CartItemEntity cartItemEntity) {
        final SqlParameterSource source = new BeanPropertySqlParameterSource(cartItemEntity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    public Optional<CartItemEntity> findById(Long id) {
        String sql = "select * from cart_item where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, cartItemRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(CartItemEntity cartItemEntity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ? ";
        jdbcTemplate.update(sql, cartItemEntity.getQuantity(), cartItemEntity.getId());
    }

    public void deleteByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return;
        }

        String sql = "DELETE FROM cart_Item WHERE id IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        namedParameterJdbcTemplate.update(sql, parameters);
    }
}
