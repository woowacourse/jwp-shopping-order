package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Money;
import cart.domain.Product;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {
    private static final RowMapper<CartItem> CART_ITEM_ROW_MAPPER = (rs, rowNum) -> {
        Long memberId = rs.getLong("member_id");
        String email = rs.getString("email");
        Long productId = rs.getLong("id");
        String name = rs.getString("name");
        int price = rs.getInt("price");
        String imageUrl = rs.getString("image_url");
        Long cartItemId = rs.getLong("cart_item.id");
        int quantity = rs.getInt("cart_item.quantity");
        Member member = new Member(memberId, email, null);
        Product product = new Product(productId, name, new Money(price), imageUrl);
        return new CartItem(cartItemId, quantity, product, member);
    };
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CartItemDao(final JdbcTemplate jdbcTemplate, final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<CartItem> findByMemberId(final Long memberId) {
        final String sql =
                "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                        +
                        "FROM cart_item " +
                        "INNER JOIN member ON cart_item.member_id = member.id " +
                        "INNER JOIN product ON cart_item.product_id = product.id " +
                        "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, CART_ITEM_ROW_MAPPER, memberId);
    }

    public Long save(final CartItem cartItem) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartItem.getMember().getId());
            ps.setLong(2, cartItem.getProduct().getId());
            ps.setInt(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public CartItem findById(final Long id) {
        final String sql =
                "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity "
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
                "SELECT cart_item.id, cart_item.member_id, member.email, product.id, product.name, product.price, product.image_url, cart_item.quantity "
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
        final String sql = "SELECT EXISTS ( "
                + "SELECT * FROM cart_item "
                + "WHERE member_id = ? "
                + "AND product_id = ? "
                + ") AS SUCCESS";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                sql,
                Boolean.class,
                memberId, productId
        ));
    }

    public void delete(final Long memberId, final Long productId) {
        final String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
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

