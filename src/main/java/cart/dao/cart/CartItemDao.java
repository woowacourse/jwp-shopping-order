package cart.dao.cart;

import cart.entity.CartItemEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<CartItemEntity> rowMapper = (rs, rowNum) -> new CartItemEntity(
            rs.getLong("id"),
            rs.getInt("quantity"),
            rs.getLong("member_id"),
            rs.getLong("product_id")
    );

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public List<CartItemEntity> findByMemberId(final Long memberId) {
        String sql = "SELECT id, quantity, member_id, product_id FROM cart_item WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Long save(final CartItemEntity cartItemEntity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_item (quantity, member_id, product_id) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setInt(1, cartItemEntity.getQuantity());
            ps.setLong(2, cartItemEntity.getMemberId());
            ps.setLong(3, cartItemEntity.getProductId());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<CartItemEntity> findById(final Long id) {
        String sql = "SELECT id, quantity, member_id, product_id FROM cart_item WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<CartItemEntity> findByIds(final List<Long> cartItemIds) {
        String sql = "SELECT id, quantity, member_id, product_id FROM cart_item " +
                "WHERE cart_item.id IN (:cartItemIds)";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("cartItemIds", cartItemIds);
        return namedParameterJdbcTemplate.query(sql, parameters, rowMapper);
    }

    public void delete(final Long memberId, final Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public void deleteById(final Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(final CartItemEntity cartItemEntity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItemEntity.getQuantity(), cartItemEntity.getId());
    }

    public void deleteByIds(List<Long> cartItemIds) {
        String sql = "DELETE FROM cart_item WHERE id = ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, cartItemIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return cartItemIds.size();
            }
        });
    }
}

