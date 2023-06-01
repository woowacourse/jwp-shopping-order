package cart.dao;

import cart.dao.entity.CartItemEntity;
import cart.domain.CartItem;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private static final RowMapper<CartItemEntity> ROW_MAPPER = (rs, rowNum) -> new CartItemEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity"));

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CartItemDao(final JdbcTemplate jdbcTemplate, final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "product_id", "quantity");
    }

    public List<CartItemEntity> findByMemberId(final Long memberId) {
        final String sql =
                "SELECT id, member_id, product_id, quantity "
                        + "FROM cart_item "
                        + "WHERE member_id = ?";
        return jdbcTemplate.query(sql, ROW_MAPPER, memberId);
    }

    public Long save(final CartItemEntity cartItem) {
        return simpleJdbcInsert.executeAndReturnKey(Map.of(
                "member_id", cartItem.getMemberId(),
                "product_id", cartItem.getProductId(),
                "quantity", cartItem.getQuantity()
        )).longValue();
    }

    public Optional<CartItemEntity> findById(final Long id) {
        final String sql = "SELECT id, member_id, product_id, quantity "
                + "FROM cart_item "
                + "WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public boolean isExist(final Long memberId, final Long productId) {
        final String sql = "SELECT EXISTS (SELECT * FROM cart_item "
                + "WHERE member_id = ? "
                + "AND product_id = ?) AS SUCCESS";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                memberId, productId
        ));
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteByIds(final List<Long> ids) {
        final String sql = "DELETE FROM cart_item WHERE id IN (:ids)";
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    public void updateQuantity(final CartItem cartItem) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }
}
