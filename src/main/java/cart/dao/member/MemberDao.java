package cart.dao.member;

import cart.entity.MemberEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<MemberEntity> rowMapper = (rs, rowNum) -> new MemberEntity(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("grade"),
            rs.getInt("total_purchase_amount")
    );

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<MemberEntity> getMemberById(final Long id) {
        String sql = "SELECT id, email, password, grade, total_purchase_amount FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<MemberEntity> getMemberByEmail(final String email) {
        String sql = "SELECT id, email, password, grade, total_purchase_amount FROM member WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long addMember(final MemberEntity memberEntity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO member (email, password, grade, total_purchase_amount) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, memberEntity.getEmail());
            ps.setString(2, memberEntity.getPassword());
            ps.setString(3, "NORMAL");
            ps.setInt(4, 0);

            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void updateMember(final MemberEntity memberEntity) {
        String sql = "UPDATE member SET email = ?, password = ?, grade = ?, total_purchase_amount = ? WHERE id = ?";
        jdbcTemplate.update(sql, memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getGrade(), memberEntity.getTotalPurchaseAmount(), memberEntity.getId());
    }

    public void deleteMember(final Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<MemberEntity> getAllMembers() {
        String sql = "SELECT id, email, password, grade, total_purchase_amount from member";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
