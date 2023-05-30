package cart.dao;

import cart.dao.entity.CartItemEntity;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.ProductEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {

    private static final RowMapper<CartItemEntity> ROW_MAPPER = (rs, rowNum) -> new CartItemEntity(
            rs.getLong("cart_item.id"),
            extractMember(rs),
            extractProduct(rs),
            rs.getInt("cart_item.quantity"),
            rs.getTimestamp("cart_item.created_at").toLocalDateTime(),
            rs.getTimestamp("cart_item.updated_at").toLocalDateTime()
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "product_id", "quantity");
    }

    private static MemberEntity extractMember(ResultSet rs) throws SQLException {
        return new MemberEntity(
                rs.getLong("member.id"),
                rs.getString("member.email"),
                rs.getString("member.password"),
                rs.getInt("member.point"),
                rs.getTimestamp("member.created_at").toLocalDateTime(),
                rs.getTimestamp("member.updated_at").toLocalDateTime()
        );
    }

    private static ProductEntity extractProduct(ResultSet rs) throws SQLException {
        return new ProductEntity(
                rs.getLong("product.id"),
                rs.getString("product.name"),
                rs.getInt("product.price"),
                rs.getString("product.image_url"),
                rs.getTimestamp("product.created_at").toLocalDateTime(),
                rs.getTimestamp("product.updated_at").toLocalDateTime()
        );
    }

    public List<CartItemEntity> findByMemberId(Long memberId) {
        String sql = "SELECT cart_item.id, cart_item.member_id, cart_item.quantity, cart_item.created_at, cart_item.updated_at,"
                + " member.id, member.email, member.password, member.point, member.created_at, member.updated_at,"
                + " product.id, product.name, product.price, product.image_url, product.created_at, product.updated_at"
                + " FROM cart_item"
                + " INNER JOIN member ON cart_item.member_id = member.id"
                + " INNER JOIN product ON cart_item.product_id = product.id"
                + " WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, ROW_MAPPER, memberId);
    }

    public Optional<CartItemEntity> findByMemberIdAndProductId(Long memberId, Long productId) {
        String sql = "SELECT cart_item.id, cart_item.member_id, cart_item.quantity, cart_item.created_at, cart_item.updated_at,"
                + " member.id, member.email, member.password, member.point, member.created_at, member.updated_at,"
                + " product.id, product.name, product.price, product.image_url, product.created_at, product.updated_at"
                + " FROM cart_item"
                + " INNER JOIN member ON cart_item.member_id = member.id"
                + " INNER JOIN product ON cart_item.product_id = product.id"
                + " WHERE cart_item.member_id = ?"
                + " and cart_item.product_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, memberId, productId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<CartItemEntity> findById(Long id) {
        String sql = "SELECT cart_item.id, cart_item.member_id, cart_item.quantity, cart_item.created_at, cart_item.updated_at,"
                + " member.id, member.email, member.password, member.point, member.created_at, member.updated_at,"
                + " product.id, product.name, product.price, product.image_url, product.created_at, product.updated_at"
                + " FROM cart_item"
                + " INNER JOIN member ON cart_item.member_id = member.id"
                + " INNER JOIN product ON cart_item.product_id = product.id"
                + " WHERE cart_item.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long save(CartItemEntity cartItemEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(cartItemEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public void updateQuantity(CartItemEntity cartItemEntity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItemEntity.getQuantity(), cartItemEntity.getId());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

