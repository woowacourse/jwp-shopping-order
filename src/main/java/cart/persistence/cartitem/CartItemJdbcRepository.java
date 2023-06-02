package cart.persistence.cartitem;

import cart.application.repository.CartItemRepository;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CartItemJdbcRepository implements CartItemRepository {

    private final RowMapper<Product> productRowMapper = (rs, rowNum) ->
            new Product(rs.getLong("product.id"),
                    rs.getString("product.name"),
                    rs.getInt("product.price"),
                    rs.getString("product.image_url")
            );

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) ->
            new Member(rs.getLong("member.id"),
                    rs.getString("member.name"),
                    rs.getString("member.email"),
                    rs.getString("member.password")
            );

    private final RowMapper<CartItem> cartItemRowMapper = (rs, rowNum) ->
            new CartItem(
                    rs.getLong("cart_item.id"),
                    rs.getInt("cart_item.quantity"),
                    productRowMapper.mapRow(rs, rowNum),
                    memberRowMapper.mapRow(rs, rowNum)
            );

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CartItemJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "product_id", "quantity");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public Long createCartItem(final CartItem cartItem) {
        final SqlParameterSource parameter = new BeanPropertySqlParameterSource(cartItem);
        return simpleJdbcInsert.executeAndReturnKey(parameter).longValue();
    }

    @Override
    public Optional<CartItem> findById(final Long id) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, member.id,  member.name, member.email, member.password,product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";
        try {
            final CartItem cartItem = jdbcTemplate.queryForObject(sql, cartItemRowMapper, id);
            return Optional.of(cartItem);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public CartItems findAllCartItemsByMemberId(final Long memberId) {
        final String sql = "SELECT cart_item.id, cart_item.member_id, member.id,  member.name, member.email, member.password,product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";

        final List<CartItem> cartItems = jdbcTemplate.query(sql, cartItemRowMapper, memberId);
        return new CartItems(cartItems);
    }

    @Override
    public void updateQuantity(final CartItem cartItem) {
        final String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }

    @Override
    public void deleteById(final Long id) {
        final String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
