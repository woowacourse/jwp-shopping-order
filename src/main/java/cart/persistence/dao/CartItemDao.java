package cart.persistence.dao;

import cart.persistence.entity.CartItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CartItemDao {

    private static final RowMapper<CartItemEntity> CART_ITEM_ENTITY_MAPPER = (rs, rowNum) -> new CartItemEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity")
    );

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItemEntity> findAllByMemberId(final Long memberId) {
        final String sql = "SELECT id, member_id, product_id, quantity FROM cart_item where member_id = ?";
        return jdbcTemplate.query(sql, CART_ITEM_ENTITY_MAPPER, memberId);
    }

    public Long create(final CartItemEntity cartItemEntity) {
        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(cartItemEntity);
        return jdbcInsert.executeAndReturnKey(source).longValue();
    }

    public CartItemEntity findById(final Long id) {
        final String sql = "SELECT id, member_id, product_id, quantity FROM cart_item where id = ?";
        return jdbcTemplate.queryForObject(sql, CART_ITEM_ENTITY_MAPPER, id);
    }

    public List<CartItemEntity> findByIds(final List<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        final String sql = "SELECT id, member_id, product_id, quantity FROM cart_item WHERE id IN (:id)";
        final MapSqlParameterSource source = new MapSqlParameterSource("id", ids);
        return namedJdbcTemplate.query(sql, source, CART_ITEM_ENTITY_MAPPER);
    }

    public void delete(final Long memberId, final Long productId) {
        final String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(final CartItemEntity cartItemEntity) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItemEntity.getQuantity(), cartItemEntity.getId());
    }

    public boolean isExist(final Long memberId, final Long productId) {
        final String sql = "SELECT COUNT(*) FROM cart_item WHERE member_id = ? AND product_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, memberId, productId) > 0;
    }
}
