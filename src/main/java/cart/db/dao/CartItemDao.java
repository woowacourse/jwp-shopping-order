package cart.db.dao;

import cart.db.entity.CartItemDetailEntity;
import cart.db.entity.CartItemEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CartItemDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Long create(final CartItemEntity cartItemEntity) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(cartItemEntity);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public List<CartItemDetailEntity> findByMemberId(final Long memberId) {
        String sql = "SELECT cart_item.id, cart_item.quantity, " +
                "member.id, member.name, member.password, " +
                "product.id, product.name, product.price, product.image_url " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.member_id = ?";
        return jdbcTemplate.query(sql, new CartItemDetailEntityRowMapper(), memberId);
    }

    public List<CartItemDetailEntity> findById(final Long id) {
        String sql = "SELECT cart_item.id, cart_item.quantity, " +
                "member.id, member.name, member.password, " +
                "product.id, product.name, product.price, product.image_url " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id = ?";
        return jdbcTemplate.query(sql, new CartItemDetailEntityRowMapper(), id);
    }

    public Long countByIdsAndMemberId(final Long memberId, List<Long> ids) {
        String sql = "SELECT COUNT(*) " +
                "FROM cart_item " +
                "INNER JOIN member ON cart_item.member_id = member.id " +
                "INNER JOIN product ON cart_item.product_id = product.id " +
                "WHERE cart_item.id IN (:ids) AND member.id = (:memberId)";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("ids", ids);
        mapSqlParameterSource.addValue("memberId", memberId);
        return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Long.class);
    }

    public void updateQuantity(final CartItemEntity cartItemEntity) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, cartItemEntity.getQuantity(), cartItemEntity.getId());
    }

    public void deleteById(final Long id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteByIdsAndMemberId(final Long memberId, final List<Long> ids) {
        String sql = "DELETE FROM cart_item WHERE cart_item.id IN "
                + " (SELECT cart_item.id FROM cart_item JOIN member ON cart_item.member_id = member.id and cart_item.id in (:ids) AND member.id = (:memberId))";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("ids", ids);
        mapSqlParameterSource.addValue("memberId", memberId);
        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }


    private static class CartItemDetailEntityRowMapper implements RowMapper<CartItemDetailEntity> {
        @Override
        public CartItemDetailEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CartItemDetailEntity(
                    rs.getLong("cart_item.id"),
                    rs.getLong("member.id"), rs.getString("member.name"), rs.getString("member.password"),
                    rs.getLong("product.id"), rs.getString("product.name"), rs.getInt("product.price"), rs.getString("product.image_url"),
                    rs.getInt("cart_item.quantity")
            );
        }
    }
}

