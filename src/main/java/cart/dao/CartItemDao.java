package cart.dao;

import cart.dao.entity.CartItemEntity;
import cart.dao.entity.CartItemWithMemberAndProductEntity;
import cart.dao.entity.MemberEntity;
import cart.dao.entity.ProductEntity;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<CartItemWithMemberAndProductEntity> cartItemEntityRowMapper = (rs, rowNum) -> {
        CartItemEntity cartItemEntity = new CartItemEntity(
                rs.getLong("cart_id"),
                rs.getLong("cart_member_id"),
                rs.getLong("product_id"),
                rs.getInt("cart_quantity"),
                null, null
        );
        MemberEntity memberEntity = new MemberEntity(
                rs.getLong("cart_member_id"),
                rs.getString("member_email"),
                null,
                rs.getInt("member_point"),
                null, null);
        ProductEntity productEntity = new ProductEntity(
                rs.getLong("product_id"),
                rs.getString("product_name"),
                rs.getInt("product_price"),
                rs.getString("product_image_url"),
                null, null
        );
        return new CartItemWithMemberAndProductEntity(cartItemEntity, memberEntity, productEntity);
    };

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id")
                .withTableName("cart_item")
                .usingColumns("member_id", "product_id", "quantity");
    }

    public Long save(CartItemEntity cartItemEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(cartItemEntity)).longValue();
    }

    public List<CartItemWithMemberAndProductEntity> findByMemberId(Long memberId) {
        String sql = "SELECT ci.id AS cart_id, "
                + "ci.member_id AS cart_member_id, "
                + "ci.quantity AS cart_quantity, "
                + "m.email AS member_email, "
                + "m.point AS member_point, "
                + "p.id AS product_id, "
                + "p.name AS product_name, "
                + "p.price AS product_price, "
                + "p.image_url AS product_image_url " +
                "FROM cart_item ci " +
                "INNER JOIN member m ON ci.member_id = m.id " +
                "INNER JOIN product p ON ci.product_id = p.id " +
                "WHERE ci.member_id = ?";

        try {
            return jdbcTemplate.query(sql, cartItemEntityRowMapper, memberId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public Optional<CartItemWithMemberAndProductEntity> findById(Long id) {
        String sql = "SELECT ci.id AS cart_id, "
                + "ci.member_id AS cart_member_id, "
                + "ci.quantity AS cart_quantity, "
                + "m.id AS member_id, "
                + "m.email AS member_email, "
                + "m.point AS member_point, "
                + "p.id AS product_id, "
                + "p.name AS product_name, "
                + "p.price AS product_price, "
                + "p.image_url AS product_image_url " +
                "FROM cart_item ci " +
                "INNER JOIN member m ON ci.member_id = m.id " +
                "INNER JOIN product p ON ci.product_id = p.id " +
                "WHERE ci.id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, cartItemEntityRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int delete(Long memberId, Long productId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ? AND product_id = ?";
        return jdbcTemplate.update(sql, memberId, productId);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int updateQuantity(CartItemEntity cartItemEntity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        return jdbcTemplate.update(sql, cartItemEntity.getQuantity(), cartItemEntity.getId());
    }

    public int deleteByMemberId(final Long memberId) {
        String sql = "DELETE FROM cart_item WHERE member_id = ?";

        return jdbcTemplate.update(sql, memberId);
    }

    public void deleteByIds(final List<Long> orderIds) {
        String sql = "DELETE FROM cart_item WHERE id = ?";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long recordId = orderIds.get(i);
                ps.setLong(1, recordId);
            }

            @Override
            public int getBatchSize() {
                return orderIds.size();
            }
        });
    }
}

