package cart.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.exception.BadRequestException;
import cart.exception.ExceptionType;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql =
            "SELECT cart_item.id, cart_item.member_id, member.email, member.nickname, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                + "FROM cart_item " + "INNER JOIN member ON cart_item.member_id = member.id "
                + "INNER JOIN product ON cart_item.product_id = product.id " + "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, new Object[] {memberId}, (rs, rowNum) -> {
            String email = rs.getString("email");
            String nickname = rs.getString("nickname");
            Long productId = rs.getLong("product.id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("cart_item.quantity");
            Member member = new Member(memberId, email, null, nickname);
            Product product = new Product(productId, name, price, imageUrl);
            return new CartItem(cartItemId, quantity, product, member);
        });
    }

    public Long save(CartItem cartItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

            ps.setLong(1, cartItem.getMember().getId());
            ps.setLong(2, cartItem.getProduct().getId());
            ps.setInt(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return Objects.requireNonNull(key).longValue();
    }

    public CartItem findById(Long id) {
        String sql =
            "SELECT cart_item.id, cart_item.member_id, member.email, member.nickname, product.id, product.name, product.price, product.image_url, cart_item.quantity "
                + "FROM cart_item " + "INNER JOIN member ON cart_item.member_id = member.id "
                + "INNER JOIN product ON cart_item.product_id = product.id " + "WHERE cart_item.id = ?";
        List<CartItem> cartItems = jdbcTemplate.query(sql, new Object[] {id}, (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            String email = rs.getString("email");
            String nickname = rs.getString("nickname");
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("cart_item.quantity");
            Member member = new Member(memberId, email, null, nickname);
            Product product = new Product(productId, name, price, imageUrl);
            return new CartItem(cartItemId, quantity, product, member);
        });
        return cartItems.isEmpty() ? null : cartItems.get(0);
    }

    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateQuantity(CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }

    public List<CartItem> findByIds(List<Long> cartItemIds) {
        final String sql =
            "SELECT cart_item.id, cart_item.member_id, cart_item.product_id, member.email, member.nickname, product.name, product.price, product.image_url, cart_item.quantity"
                + " FROM cart_item INNER JOIN member ON cart_item.member_id = member.id"
                + " INNER JOIN product ON cart_item.product_id = product.id WHERE cart_item.id IN (:ids)";

        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("ids", cartItemIds);

        List<CartItem> cartItems = namedParameterJdbcTemplate.query(sql, parameterSource, (rs, rowNum) -> {
            final Long id = rs.getLong("id");

            final int quantity = rs.getInt("quantity");

            final Long productId = rs.getLong("product_id");
            final String name = rs.getString("name");
            final int price = rs.getInt("price");
            final String imageUrl = rs.getString("image_url");

            final Long memberId = rs.getLong("member_id");
            final String email = rs.getString("email");
            final String nickname = rs.getString("nickname");

            Product product = new Product(productId, name, price, imageUrl);
            Member member = new Member(memberId, email, null, nickname);
            return new CartItem(id, quantity, product, member);
        });

        if (cartItems.size() != cartItemIds.size()) {
            throw new BadRequestException(ExceptionType.CART_ITEM_NO_EXIST);
        }
        return cartItems;
    }

    public void deleteByIds(List<Long> cartItemIds) {
        String query = "DELETE FROM cart_item WHERE id = ?";

        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long cartItemId = cartItemIds.get(i);
                ps.setLong(1, cartItemId);
            }

            @Override
            public int getBatchSize() {
                return cartItemIds.size();
            }
        });
    }
}

