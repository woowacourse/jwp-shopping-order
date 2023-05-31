package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Product;
import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {
    private static final RowMapper<CartItem> CART_ITEM_ROW_MAPPER = (rs, rowNum) -> {
        Member member = new Member(rs.getLong("member_id"), rs.getString("email"), null);
        Product product = new Product(
                rs.getLong("product_id"),
                rs.getString("name"),
                new Money(rs.getInt("price")),
                rs.getString("image_url"));

        return new CartItem(
                rs.getLong("cart_item.id"),
                rs.getInt("cart_item.quantity"),
                product,
                member);
    };
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

    public List<CartItem> findByMemberId(final Long memberId) {
        final String sql =
                "SELECT cart_item.id, cart_item.member_id, member.email, " +
                        "product.id as product_id, product.name, product.price, product.image_url, cart_item.quantity "
                        +
                        "FROM cart_item " +
                        "INNER JOIN member ON cart_item.member_id = member.id " +
                        "INNER JOIN product ON cart_item.product_id = product.id " +
                        "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, CART_ITEM_ROW_MAPPER, memberId);
    }

    public Long save(final CartItem cartItem) {
        final Number createdId = simpleJdbcInsert.executeAndReturnKey(Map.of(
                "member_id", cartItem.getMember().getId(),
                "product_id", cartItem.getProduct().getId(),
                "quantity", cartItem.getQuantity()
        ));

        return createdId.longValue();
    }

    public CartItem findById(final Long id) {
        final String sql =
                "SELECT cart_item.id, cart_item.member_id, member.email, " +
                        "product.id as product_id, product.name, product.price, product.image_url, cart_item.quantity "
                        +
                        "FROM cart_item " +
                        "INNER JOIN member ON cart_item.member_id = member.id " +
                        "INNER JOIN product ON cart_item.product_id = product.id " +
                        "WHERE cart_item.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, CART_ITEM_ROW_MAPPER, id);
        } catch (final EmptyResultDataAccessException exception) {
            return null;
        }
    }

    public List<CartItem> findByIds(final List<Long> ids) {
        final String sql =
                "SELECT cart_item.id, cart_item.member_id, member.email, " +
                        "product.id as product_id, product.name, product.price, product.image_url, cart_item.quantity "
                        +
                        "FROM cart_item " +
                        "INNER JOIN member ON cart_item.member_id = member.id " +
                        "INNER JOIN product ON cart_item.product_id = product.id " +
                        "WHERE cart_item.id IN (:ids)";
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        return namedParameterJdbcTemplate.query(sql, parameters, CART_ITEM_ROW_MAPPER);
    }

    public boolean isExist(final Long memberId, final Long productId) {
        final String sql = "SELECT EXISTS ( " +
                "SELECT * FROM cart_item " +
                "WHERE member_id = ? " +
                "AND product_id = ? " +
                ") AS SUCCESS";
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
