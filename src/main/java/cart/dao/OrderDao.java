package cart.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import cart.domain.Member;
import cart.entity.OrderEntity;

@Repository
public class OrderDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<OrderEntity> rowMapper = (rs, rowNum) -> {
        final long id = rs.getLong("id");
        final long memberId = rs.getLong("member_id");
        final int price = rs.getInt("price");
        return new OrderEntity(id, price, memberId);
    };

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long addOrder(OrderEntity order) {
        final String sql = "INSERT INTO `order` (member_id, price) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, order.getMemberId());
            ps.setInt(2, order.getPrice());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<OrderEntity> findById(Long orderId) {
        try {
            final String sql = "SELECT * FROM `order` WHERE id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, orderId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<OrderEntity> findByMember(Member member) {
        final String sql = "SELECT * FROM `order` WHERE member_id = ?";
        return jdbcTemplate.query(sql, rowMapper, member.getId());
    }
}
