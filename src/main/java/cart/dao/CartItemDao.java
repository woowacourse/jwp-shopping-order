package cart.dao;

import cart.dao.dto.CartItemProductDto;
import cart.dao.entity.CartItemEntity;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private static final RowMapper<CartItemProductDto> ITEM_PRODUCT_ROW_MAPPER = (rs, rowNum) ->
        new CartItemProductDto(
            rs.getLong("cart_item.id"),
            rs.getLong("product.id"),
            rs.getLong("member.id"),
            rs.getString("email"),
            rs.getInt("cart_item.quantity"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getString("image_url"));

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("cart_item")
            .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public List<CartItemProductDto> findByMemberId(Long memberId) {
        String sql =
            "SELECT cart_item.id, cart_item.member_id, member.id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, ITEM_PRODUCT_ROW_MAPPER, memberId);
    }

    public long save(CartItemEntity cartItemEntity) {
        Number generatedKey = insertAction.executeAndReturnKey(
            Map.of("member_id", cartItemEntity.getMemberId(),
                "product_id", cartItemEntity.getProductId(),
                "quantity", cartItemEntity.getQuantity()));

        return Objects.requireNonNull(generatedKey).longValue();
    }

    public Optional<CartItemProductDto> findById(long id) {
        String sql =
            "SELECT cart_item.id, cart_item.member_id, member.email, member.id, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
                ITEM_PRODUCT_ROW_MAPPER, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(CartItemEntity cartItemEntity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        int quantity = cartItemEntity.getQuantity();
        jdbcTemplate.update(sql, quantity, cartItemEntity.getId());
    }

    public boolean isNonExistingId(long cartItemId) {
        String sql = "SELECT EXISTS(SELECT * FROM cart_item WHERE id = ?) "
            + "AS cart_item_exist";
        try {
            return Boolean.FALSE.equals(
                jdbcTemplate.queryForObject(sql, new Object[]{cartItemId}, Boolean.class));
        } catch (EmptyResultDataAccessException exception) {
            return true;
        }
    }

    public List<CartItemProductDto> findAllByIds(List<Long> ids) {
        String sql = "SELECT cart_item.id, cart_item.member_id, cart_item.quantity, " +
            "member.email, member.id, product.id, product.name, product.price, product.image_url " +
            "FROM cart_item " +
            "LEFT JOIN product ON cart_item.product_id = product.id " +
            "LEFT JOIN member ON cart_item.member_id = member.id " +
            "WHERE cart_item.id IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        return namedParameterJdbcTemplate.query(sql, parameters, ITEM_PRODUCT_ROW_MAPPER);
    }

    public void removeAllByIds(List<Long> ids) {
        String sql = "DELETE FROM cart_item WHERE id IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        namedParameterJdbcTemplate.update(sql, parameters);
    }
}

