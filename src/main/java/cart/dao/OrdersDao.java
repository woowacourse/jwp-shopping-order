package cart.dao;

import cart.dao.entity.OrdersEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class OrdersDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<OrdersEntity> ordersEntityRowMapper = (rs, rowNum) -> new OrdersEntity(
            rs.getLong("id"),
            rs.getLong("member_id"),
            rs.getInt("price"),
            rs.getBoolean("confirm_state")
    );


    public OrdersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public Long createOrders(final long memberId, final int discountPrice) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO orders(member_id,price,confirm_state) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setLong(1, memberId);
            ps.setInt(2, discountPrice);
            ps.setBoolean(3, false);


            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<OrdersEntity> findAllByMemberId(final long memberId) {
        final String sql = "SELECT * FROM orders WHERE member_id = ?";
        try {
            return jdbcTemplate.query(sql, ordersEntityRowMapper, memberId);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<OrdersEntity>();
        }
    }

    public Optional<OrdersEntity> findById(final long id) {
        final String sql = "SELECT * FROM orders WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ordersEntityRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateConfirmById(final long id) {
        final String sql = "UPDATE orders SET confirm_state = true WHERE id = ? ";
        jdbcTemplate.update(sql, id);
    }

    public void deleteById(final long id) {
        final String sql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
