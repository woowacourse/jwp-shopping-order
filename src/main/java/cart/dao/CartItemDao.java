package cart.dao;

import cart.dao.dto.CartItemWithProductDto;
import cart.entity.CartItemEntity;
import cart.exception.CartItemNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CartItemDao {

    private static final int SINGLE_AFFECTED_ROW_NUMBER = 1;
    private static final RowMapper<CartItemWithProductDto> cartItemWithProductRowMapper = (rs, rn) ->
            new CartItemWithProductDto(
                    rs.getLong("cart_item.id"),
                    rs.getLong("cart_item.member_id"),
                    rs.getLong("product.id"),
                    rs.getString("product.name"),
                    rs.getInt("product.price"),
                    rs.getString("product.image_url"),
                    rs.getInt("cart_item.quantity")
            );

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public CartItemDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItemWithProductDto> findProductDetailByIds(final List<Long> ids) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id IN (:ids)";
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
        final List<CartItemWithProductDto> results = namedParameterJdbcTemplate.query(sql, params, cartItemWithProductRowMapper);
        validateResultSize(ids.size(), results.size());
        return results;
    }

    private void validateResultSize(final int sourceSize, final int resultSize) {
        if (sourceSize != resultSize) {
            throw new CartItemNotFoundException(
                    String.format("존재하지 않는 cartItem 이 %s개 있습니다. ", sourceSize - resultSize));
        }
    }

    public List<CartItemWithProductDto> findAllDetailByMemberId(final long memberId) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ? " +
                "ORDER BY cart_item.id";
        return jdbcTemplate.query(sql, cartItemWithProductRowMapper, memberId);
    }

    public long save(final CartItemEntity source) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(source);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    // TODO: quantity 가 바뀌지 않는 경우에 다른 예외로 처리 필요.
    public void updateQuantity(final long id, final int quantity) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        final int affectedRowNumber = jdbcTemplate.update(sql, quantity, id);
        validateAffectedRowNumber(affectedRowNumber, SINGLE_AFFECTED_ROW_NUMBER);
    }

    public void deleteById(final long id) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        final int affectedRowNumber = jdbcTemplate.update(sql, id);
        validateAffectedRowNumber(affectedRowNumber, SINGLE_AFFECTED_ROW_NUMBER);
    }

    public void deleteAllByIds(final List<Long> ids) {
        final String sql = "DELETE FROM cart_item WHERE id IN (:ids)";
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
        final int affectedRowNumber = namedParameterJdbcTemplate.update(sql, params);
        validateAffectedRowNumber(affectedRowNumber, ids.size());
    }

    private void validateAffectedRowNumber(final int affectedRowNumber, final int expectedNumber) {
        if (affectedRowNumber != expectedNumber) {
            throw new CartItemNotFoundException(
                    String.format("존재하지 않는 cartItem 이 %s개 있습니다. ", expectedNumber - affectedRowNumber));
        }
    }

    public Long findMemberIdById(final long id) {
        final String sql = "SELECT member_id FROM cart_item WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, id);
        } catch (EmptyResultDataAccessException e) {
            throw new CartItemNotFoundException();
        }
    }
}

