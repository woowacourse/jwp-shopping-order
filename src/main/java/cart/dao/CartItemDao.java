package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<CartItem> rowMapper = (rs, rowNum) -> {
        Long productId = rs.getLong("p_id");
        String name = rs.getString("name");
        int price = rs.getInt("price");
        String imageUrl = rs.getString("image_url");
        Product product = new Product(productId, name, price, imageUrl);

        long memberId = rs.getLong("member_id");
        String email = rs.getString("email");
        long point = rs.getLong("point");
        Member member = new Member(memberId, email, null, point);

        Long cartItemId = rs.getLong("ci_id");
        int quantity = rs.getInt("quantity");
        return new CartItem(cartItemId, quantity, product, member);
    };

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql = "SELECT ci.id AS ci_id, ci.member_id, m.email, p.id AS p_id, p.name, p.price, p.image_url, ci.quantity, mp.point " +
                "FROM cart_item AS ci " +
                "INNER JOIN member AS m ON ci.member_id = m.id " +
                "INNER JOIN product AS p ON ci.product_id = p.id " +
                "INNER JOIN member_point AS mp ON ci.member_id = mp.member_id " +
                "WHERE ci.member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public Long save(CartItem cartItem) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("member_id", cartItem.getMember().getId());
        mapSqlParameterSource.addValue("product_id", cartItem.getProduct().getId());
        mapSqlParameterSource.addValue("quantity", cartItem.getQuantity());
        return simpleJdbcInsert.executeAndReturnKey(mapSqlParameterSource).longValue();
    }

    public Optional<CartItem> findById(Long id) {
        String sql = "SELECT ci.id AS ci_id, ci.member_id, m.email, p.id AS p_id, p.name, p.price, p.image_url, ci.quantity, mp.point " +
                "FROM cart_item AS ci " +
                "INNER JOIN member AS m ON ci.member_id = m.id " +
                "INNER JOIN product AS p ON ci.product_id = p.id " +
                "INNER JOIN member_point AS mp ON ci.member_id = mp.member_id " +
                "WHERE ci.id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findAny();
    }

    public void deleteByMemberAndProduct(Long memberId, Long productId) {
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
}

