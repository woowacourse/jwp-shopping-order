package cart.dao;

import cart.dao.rowmapper.CartItemRowMapper;
import cart.domain.cartitem.CartItem;
import cart.dao.entity.CartItemEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cart.dao.support.SqlHelper.sqlHelper;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public Long insertCartItem(CartItemEntity cartItemEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                        .addValue("member_id", cartItemEntity.getMemberId())
                        .addValue("product_id", cartItemEntity.getProductId())
                        .addValue("quantity", cartItemEntity.getQuantity()))
                .longValue();
    }

    public List<CartItem> findByCartItemIds(List<Long> cartItemIds) {
        String collectCartItemIds = cartItemIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        String sql = sqlHelper()
                .select()
                .columns("ci.id, ci.member_id, ci.quantity")
                .columns(", m.id, m.email, m.password, m.money, m.point")
                .columns(", p.id, p.name, p.price, p.image_url")
                .from().table("cart_item ci")
                .innerJoin("member m").on("ci.member_id = m.id")
                .innerJoin("product p").on("ci.product_id = p.id")
                .where().in("ci.id", collectCartItemIds)
                .toString();

        return jdbcTemplate.query(sql, CartItemRowMapper.joinMemberAndProduct);
    }

    public Optional<CartItem> findByCartItemId(Long cartItemId) {
        String sql = sqlHelper()
                .select()
                .columns("ci.id, ci.member_id, ci.quantity")
                .columns(", m.id, m.email, m.password, m.money, m.point")
                .columns(", p.id, p.name, p.price, p.image_url")
                .from().table("cart_item ci")
                .innerJoin("member m").on("ci.member_id = m.id")
                .innerJoin("product p").on("ci.product_id = p.id")
                .where().condition("ci.id = ?")
                .toString();

        try {
            CartItem cartItem = jdbcTemplate.queryForObject(sql, CartItemRowMapper.joinMemberAndProduct, cartItemId);
            return Optional.ofNullable(cartItem);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<CartItem> findByMemberId(Long memberId) {
        String sql = sqlHelper()
                .select()
                .columns("ci.id, ci.member_id, ci.quantity")
                .columns(", m.id, m.email, m.password, m.money, m.point")
                .columns(", p.id, p.name, p.price, p.image_url")
                .from().table("cart_item ci")
                .innerJoin("member m").on("ci.member_id = m.id")
                .innerJoin("product p").on("ci.product_id = p.id")
                .where().condition("ci.member_id = ?")
                .toString();


        return jdbcTemplate.query(sql, CartItemRowMapper.joinMemberAndProduct, memberId);
    }

    public void updateQuantity(Long cartItemId, int quantity) {
        String sql = sqlHelper()
                .update().table("cart_item")
                .set("quantity = ?")
                .where().condition("id = ?")
                .toString();

        jdbcTemplate.update(sql, quantity, cartItemId);
    }

    public void deleteByCartItemId(Long cartItemId) {
        String sql = sqlHelper()
                .delete()
                .from().table("cart_item")
                .where().condition("id = ?")
                .toString();

        jdbcTemplate.update(sql, cartItemId);
    }

    public void delete(Long memberId, Long productId) {
        String sql = sqlHelper()
                .delete()
                .from().table("cart_item")
                .where().condition("member_id = ? AND product_id = ?")
                .toString();

        jdbcTemplate.update(sql, memberId, productId);
    }
}
