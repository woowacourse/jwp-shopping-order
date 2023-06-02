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
                .columns("cart_item.id, cart_item.member_id, cart_item.quantity")
                .columns(", member.id, member.email, member.password, member.money, member.point")
                .columns(", product.id, product.name, product.price, product.image_url")
                .from().table("cart_item")
                .innerJoin("member").on("cart_item.member_id = member.id")
                .innerJoin("product").on("cart_item.product_id = product.id")
                .where().in("cart_item.id", collectCartItemIds)
                .toString();

        return jdbcTemplate.query(sql, CartItemRowMapper.joinMemberAndProduct);
    }

    public Optional<CartItem> findByCartItemId(Long cartItemId) {
        String sql = sqlHelper()
                .select()
                .columns("cart_item.id, cart_item.member_id, cart_item.quantity")
                .columns(", member.id, member.email, member.password, member.money, member.point")
                .columns(", product.id, product.name, product.price, product.image_url")
                .from().table("cart_item")
                .innerJoin("member").on("cart_item.member_id = member.id")
                .innerJoin("product").on("cart_item.product_id = product.id")
                .where().condition("cart_item.id = ?")
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
                .columns("cart_item.id, cart_item.member_id, cart_item.quantity")
                .columns(", member.id, member.email, member.password, member.money, member.point")
                .columns(", product.id, product.name, product.price, product.image_url")
                .from().table("cart_item")
                .innerJoin("member").on("cart_item.member_id = member.id")
                .innerJoin("product").on("cart_item.product_id = product.id")
                .where().condition("cart_item.member_id = ?")
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

    public void deleteByCateItemIds(List<Long> cartItemIds) {
        String collectCartItemIds = cartItemIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        String sql = sqlHelper()
                .delete()
                .from().table("cart_item")
                .where().in("id", collectCartItemIds)
                .toString();

        jdbcTemplate.update(sql);
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
