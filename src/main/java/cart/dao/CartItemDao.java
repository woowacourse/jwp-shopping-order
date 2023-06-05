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
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<CartItemEntity> rowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final Long memberId = rs.getLong("member_id");
        final Long productId = rs.getLong("product_id");
        final int quantity = rs.getInt("quantity");
        return new CartItemEntity(id, memberId, productId, quantity);
    };

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingColumns("quantity", "member_id", "product_id")
                .usingGeneratedKeyColumns("id");
    }

    public CartItemEntity insert(final CartItemEntity cartItemEntity) {
        final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(cartItemEntity);
        final long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new CartItemEntity(
                id,
                cartItemEntity.getMemberId(),
                cartItemEntity.getProductId(),
                cartItemEntity.getQuantity()
        );
    }

    @Transactional(readOnly = true)
    public List<CartItemEntity> findAllByMemberId(final Long memberId) {
        String sql = "SELECT * FROM cart_item WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    @Transactional(readOnly = true)
    public Optional<CartItemEntity> findById(Long id) {
        String sql = "SELECT * FROM cart_item WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int deleteById(final Long cartItemId) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        return jdbcTemplate.update(sql, cartItemId);
    }

    public void deleteByIds(final List<Long> cartItemIds) {
        if (cartItemIds.isEmpty()) {
            return;
        }
        final String sql = "DELETE FROM cart_item WHERE id IN (:ids)";
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("ids", cartItemIds);
        namedJdbcTemplate.update(sql, parameterSource);
    }

    public void updateQuantity(final CartItemEntity cartItemEntity) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItemEntity.getQuantity(), cartItemEntity.getId());
    }
}
