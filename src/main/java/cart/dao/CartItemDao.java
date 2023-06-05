package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class CartItemDao {

    private final JdbcTemplate jdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(CartItem cartItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO tb_cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, cartItem.getMember().getId());
            ps.setLong(2, cartItem.getProduct().getId());
            ps.setInt(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql = "SELECT tb_cart_item.id, tb_cart_item.member_id, tb_member.email, tb_product.id, tb_product.name, tb_product.price, tb_product.image_url, tb_cart_item.quantity " +
                "FROM tb_cart_item " +
                "INNER JOIN tb_member ON tb_cart_item.member_id = tb_member.id " +
                "INNER JOIN tb_product ON tb_cart_item.product_id = tb_product.id " +
                "WHERE tb_cart_item.member_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            String email = rs.getString("email");
            Long productId = rs.getLong("tb_product.id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("tb_cart_item.id");
            int quantity = rs.getInt("quantity");
            Member member = new Member(memberId, email, null);
            Product product = new Product(productId, name, price, imageUrl);
            return new CartItem(cartItemId, quantity, member, product);
        }, memberId);
    }

    public Optional<CartItem> findById(Long id) {
        String sql = "SELECT tb_cart_item.id, tb_cart_item.member_id, tb_member.email, tb_product.id, tb_product.name, tb_product.price, tb_product.image_url, tb_cart_item.quantity " +
                "FROM tb_cart_item " +
                "INNER JOIN tb_member ON tb_cart_item.member_id = tb_member.id " +
                "INNER JOIN tb_product ON tb_cart_item.product_id = tb_product.id " +
                "WHERE tb_cart_item.id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            String email = rs.getString("email");
            Long productId = rs.getLong("tb_product.id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("tb_cart_item.id");
            int quantity = rs.getInt("quantity");
            Member member = new Member(memberId, email, null);
            Product product = new Product(productId, name, price, imageUrl);
            return new CartItem(cartItemId, quantity, member, product);
        }, id)
                .stream()
                .findAny();
    }

    public boolean isExistBy(Long memberId, Long productId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM tb_cart_item WHERE member_id = ? AND product_id = ?)";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, memberId, productId));
    }

    public void updateQuantity(CartItem cartItem) {
        String sql = "UPDATE tb_cart_item SET quantity = ? WHERE id = ?";

        jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM tb_cart_item WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    public void deleteByIds(List<Long> ids) {
        String sql = "DELETE FROM tb_cart_item WHERE id IN (" + mapToSqlIds(ids) + ")";

        jdbcTemplate.update(sql);
    }

    private String mapToSqlIds(List<Long> ids) {
        return ids.stream()
               .map(String::valueOf)
               .collect(Collectors.joining(", "));
    }
}
