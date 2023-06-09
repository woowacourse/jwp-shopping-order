package cart.dao;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.exception.CartItemException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.money, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.deleted = false AND cart_item.member_id = ?";
        return jdbcTemplate.query(sql, new Object[]{memberId}, (rs, rowNum) -> {
            String email = rs.getString("email");
            Long productId = rs.getLong("product.id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("cart_item.quantity");
            int money = rs.getInt("money");
            Member member = new Member(memberId, email, null, money);
            Product product = new Product(productId, name, price, imageUrl);
            return new CartItem(cartItemId, quantity, product, member);
        });
    }

    public Long save(CartItem cartItem) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO cart_item (member_id, product_id, quantity) VALUES (?, ?, ?)",
                    new String[]{"id"}
            );

            ps.setLong(1, cartItem.getMember().getId());
            ps.setLong(2, cartItem.getProduct().getId());
            ps.setInt(3, cartItem.getQuantity());

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public CartItem findById(Long id) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.money, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.deleted = false AND cart_item.id = ?";
        List<CartItem> cartItems = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            String email = rs.getString("email");
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("cart_item.quantity");
            int money = rs.getInt("money");
            Member member = new Member(memberId, email, null, money);
            Product product = new Product(productId, name, price, imageUrl);
            return new CartItem(cartItemId, quantity, product, member);
        });
        return cartItems.isEmpty() ? null : cartItems.get(0);
    }


    public List<CartItem> findItemsByIds(List<Long> itemIds) {
        String sql = "SELECT cart_item.id, cart_item.member_id, member.email, member.money, product.id, product.name, product.price, product.image_url, cart_item.quantity " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.deleted = false AND cart_item.id IN (" + StringUtils.collectionToDelimitedString(itemIds, ",") + ")";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long memberId = rs.getLong("member_id");
            String email = rs.getString("email");
            Long productId = rs.getLong("id");
            String name = rs.getString("name");
            int price = rs.getInt("price");
            String imageUrl = rs.getString("image_url");
            Long cartItemId = rs.getLong("cart_item.id");
            int quantity = rs.getInt("cart_item.quantity");
            int money = rs.getInt("member.money");
            Member member = new Member(memberId, email, null, money);
            Product product = new Product(productId, name, price, imageUrl);
            return new CartItem(cartItemId, quantity, product, member);
        });
    }


    public void delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        jdbcTemplate.update(sql, memberId, productId);
    }

    public void deleteById(Long id) {
        String sql = "UPDATE cart_item SET deleted = true WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    public void deleteByIds(List<Long> cartItemIds) throws CartItemException {
        String sql = "UPDATE cart_item SET deleted = true WHERE id = ?;";
        List<Object[]> deleteIds = new ArrayList<>();

        for (Long id : cartItemIds) {
            deleteIds.add(new Object[]{id});
        }

        int[] ints = jdbcTemplate.batchUpdate(sql, deleteIds);

        boolean hasZero = Arrays.stream(ints).anyMatch(i -> i == 0);
        if (hasZero) {
            throw new CartItemException.NotFoundException("장바구니 상품을 찾을 수 없어 삭제가 실패했습니다.");
        }
    }

    public void updateQuantity(CartItem cartItem) throws CartItemException {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        int updated = jdbcTemplate.update(sql, cartItem.getQuantity(), cartItem.getId());
        if (updated < 1) {
            throw new CartItemException.NotFoundException("해당 장바구니 상품을 찾을 수 없어 수량을 변경할 수 없습니다.");
        }
    }

}

