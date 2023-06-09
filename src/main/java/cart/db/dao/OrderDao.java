package cart.db.dao;

import cart.db.entity.OrderEntity;
import cart.db.entity.OrderMemberDetailEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("`order`")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "total_price", "discounted_total_price", "delivery_price", "ordered_at");
    }

    public Long create(final OrderEntity orderEntity) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(orderEntity);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public Optional<OrderMemberDetailEntity> findOrderMemberById(final Long id) {
        String sql = "SELECT `order`.id, member_id, member.name, member.password, " +
                "total_price, discounted_total_price, delivery_price, ordered_at " +
                "FROM `order` JOIN member ON `order`.member_id = member.id WHERE `order`.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new OrderMemberDetailEntityRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<OrderMemberDetailEntity> findOrderMemberByMemberId(final Long memberId) {
        String sql = "SELECT `order`.id, member_id, member.name, member.password, " +
                "total_price, discounted_total_price, delivery_price, ordered_at " +
                "FROM `order` JOIN member ON `order`.member_id = member.id WHERE member_id = ?";
        return jdbcTemplate.query(sql, new OrderMemberDetailEntityRowMapper(), memberId);
    }

    private static class OrderEntityRowMapper implements RowMapper<OrderEntity> {
        @Override
        public OrderEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrderEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getInt("total_price"),
                    rs.getInt("discounted_total_price"),
                    rs.getInt("delivery_price"),
                    rs.getTimestamp("ordered_at").toLocalDateTime()
            );
        }
    }

    private static class OrderMemberDetailEntityRowMapper implements RowMapper<OrderMemberDetailEntity> {
        @Override
        public OrderMemberDetailEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrderMemberDetailEntity(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getInt("total_price"),
                    rs.getInt("discounted_total_price"),
                    rs.getInt("delivery_price"),
                    rs.getTimestamp("ordered_at").toLocalDateTime()
            );
        }
    }
}
