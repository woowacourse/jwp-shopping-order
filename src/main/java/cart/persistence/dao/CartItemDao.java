package cart.persistence.dao;

import cart.persistence.dto.CartDetailDTO;
import cart.persistence.entity.CartItemEntity;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate,
            final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Long create(CartItemEntity cartItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartItem.getMemberId());
            ps.setLong(2, cartItem.getProductId());
            ps.setInt(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Optional<CartDetailDTO> findById(final long id) {
        String sql = "SELECT * FROM cart_item "
                + "INNER JOIN member ON cart_item.member_id = member.id "
                + "INNER JOIN product ON cart_item.product_id = product.id "
                + "WHERE cart_item.id = ?";
        try {
            CartDetailDTO cartDetail = jdbcTemplate.queryForObject(sql, RowMapperHelper.cartDetailRowMapper(), id);
            return Optional.of(cartDetail);
        } catch (IncorrectResultSizeDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<CartDetailDTO> findByMemberId(final long memberId) {
        String sql = "SELECT * FROM cart_item "
                + "INNER JOIN member ON cart_item.member_id = member.id "
                + "INNER JOIN product ON cart_item.product_id = product.id "
                + "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, RowMapperHelper.cartDetailRowMapper(), memberId);
    }

    public List<CartDetailDTO> findByIds(final List<Long> ids) {
        String sql = "SELECT * FROM cart_item "
                + "INNER JOIN member ON cart_item.member_id = member.id "
                + "INNER JOIN product ON cart_item.product_id = product.id "
                + "WHERE cart_item.id IN (:ids)";

        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        return namedParameterJdbcTemplate.query(sql, parameters, RowMapperHelper.cartDetailRowMapper());
    }

    public void updateQuantity(final CartItemEntity cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }

    public void deleteById(final long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteByIds(final List<Long> ids) {
        String sql = "DELETE FROM cart_item WHERE id IN (:ids)";

        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        namedParameterJdbcTemplate.update(sql, parameters);
    }
}

