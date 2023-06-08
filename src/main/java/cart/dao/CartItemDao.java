package cart.dao;

import cart.dao.entity.CartItemEntity;
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

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "product_id", "quantity");
    }

    public Optional<CartItemEntity> findByIdForMember(final long memberId, final Long id) {
        final String sql = "SELECT id, member_id, product_id, quantity "
                + "FROM cart_item "
                + "WHERE member_id = ? "
                + "AND id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, memberId, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<CartItemEntity> findByMemberId(final long memberId) {
        final String sql =
                "SELECT id, member_id, product_id, quantity "
                        + "FROM cart_item "
                        + "WHERE member_id = ?";
        return jdbcTemplate.query(sql, ROW_MAPPER, memberId);
    }

    public long save(final CartItemEntity cartItem) {
        return simpleJdbcInsert.executeAndReturnKey(Map.of(
                "member_id", cartItem.getMemberId(),
                "product_id", cartItem.getProductId(),
                "quantity", cartItem.getQuantity()
        )).longValue();
    }

    public boolean isExist(final long memberId, final long productId) {
        final String sql = "SELECT EXISTS (SELECT * FROM cart_item "
                + "WHERE member_id = ? "
                + "AND product_id = ?) AS SUCCESS";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                memberId, productId
        ));
    }

    public void deleteById(final long id) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteByIds(final List<Long> ids) {
        final String sql = "DELETE FROM cart_item WHERE id IN (:ids)";
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    public void updateQuantity(final CartItemEntity cartItem) {
        final String sql = "UPDATE cart_item SET quantity = ? "
                + "WHERE id = ? "
                + "AND member_id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId(), cartItem.getMemberId());
    }

    public List<CartItemEntity> findAll() {
        final String sql = "SELECT id, member_id, product_id, quantity "
                + "FROM cart_item ";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }
}
