package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class CartItemDao {
    private static final RowMapper<CartItem> CART_ITEM_ROW_MAPPER = (rs, rowNum) -> {
        Long memberId = rs.getLong("member_id");
        String email = rs.getString("email");
        Long productId = rs.getLong("product_id");
        String name = rs.getString("name");
        int price = rs.getInt("price");
        String imageUrl = rs.getString("image_url");
        Long cartItemId = rs.getLong("id");
        int quantity = rs.getInt("quantity");
        Member member = new Member(memberId, email, null);
        Product product = new Product(productId, name, price, imageUrl);
        return new CartItem(cartItemId, quantity, product, member);
    };
    private final JdbcTemplate jdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CartItem> findByMemberId(Long memberId) {
        final String sql = makeSelectQuery("WHERE c.member_id = ?");

        return jdbcTemplate.query(sql, CART_ITEM_ROW_MAPPER, memberId);
    }

    private String makeSelectQuery(final String whereClause) {
        return "SELECT c.id, c.member_id, m.email, p.id AS product_id, p.name, p.price, p.image_url, c.quantity " +
                "FROM cart_item c " +
                "INNER JOIN member m ON c.member_id = m.id " +
                "INNER JOIN product p ON c.product_id = p.id "
                + whereClause;
    }

    public Long save(CartItem cartItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
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

    public CartItem findById(Long id) {
        final String sql = makeSelectQuery("WHERE c.id = ?");

        try {
            return jdbcTemplate.queryForObject(sql, CART_ITEM_ROW_MAPPER, id);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 장바구니 아이템입니다.");
        }
    }

    public List<CartItem> findAllByIds(final List<Long> ids) {
        final String cartItemIds = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        final String whereClause = "WHERE c.id IN " + "(" + cartItemIds + ")";
        final String sql = makeSelectQuery(whereClause);

        return jdbcTemplate.query(sql, CART_ITEM_ROW_MAPPER);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteAllByIds(final List<Long> ids) {
        final String query = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        final String sql = "DELETE FROM cart_item WHERE id IN (" + query + ")";
        jdbcTemplate.update(sql);
    }

    public void updateQuantity(CartItem cartItem) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }
}

