package cart.dao;

import cart.dao.entity.MemberEntity;
import cart.dao.entity.OrderEntity;
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
public class OrderDao {

    private static final RowMapper<OrderEntity> ROW_MAPPER = (rs, rowNum) -> new OrderEntity(
            rs.getLong("orders.id"),
            extractMember(rs),
            rs.getInt("orders.used_point"),
            rs.getInt("orders.saved_point"),
            rs.getInt("orders.delivery_fee"),
            rs.getTimestamp("orders.created_at").toLocalDateTime(),
            rs.getTimestamp("orders.updated_at").toLocalDateTime()
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id")
                .usingColumns("member_id", "used_point", "saved_point", "delivery_fee");
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

    public List<OrderEntity> findAllByMemberId(Long memberId) {
        String sql =
                "SELECT orders.id, orders.used_point, orders.saved_point, orders.created_at, orders.delivery_fee, orders.updated_at,"
                        + " member.id, member.email, member.password, member.point, member.created_at, member.updated_at"
                        + " FROM orders"
                        + " INNER JOIN member ON orders.member_id = member.id"
                        + " WHERE orders.member_id = ?";
        return jdbcTemplate.query(sql, ROW_MAPPER, memberId);
    }

    public Optional<OrderEntity> findById(Long id) {
        String sql =
                "SELECT orders.id, orders.used_point, orders.saved_point, orders.created_at, orders.delivery_fee, orders.updated_at,"
                        + " member.id, member.email, member.password, member.point, member.created_at, member.updated_at"
                        + " FROM orders"
                        + " INNER JOIN member ON orders.member_id = member.id"
                        + " WHERE orders.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long save(OrderEntity orderEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(orderEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }
}
