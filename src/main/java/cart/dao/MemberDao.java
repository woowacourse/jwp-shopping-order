package cart.dao;

import cart.dao.entity.MemberEntity;
import cart.domain.Member;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private static final RowMapper<MemberEntity> ROW_MAPPER = (rs, rowNum) -> new MemberEntity(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("point"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
    );

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MemberEntity save(MemberEntity memberEntity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO member(email, password, point) VALUES(?, ?, ?)";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    pstm.setString(1, memberEntity.getEmail());
                    pstm.setString(2, memberEntity.getPassword());
                    pstm.setInt(3, memberEntity.getPoint());
                    return pstm;
                },
                keyHolder
        );

        Map<String, Object> keys = keyHolder.getKeys();
        return new MemberEntity(
                (Long) keys.get("ID"),
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getPoint(),
                ((Timestamp) keys.get("CREATED_AT")).toLocalDateTime(),
                ((Timestamp) keys.get("UPDATED_AT")).toLocalDateTime()
        );
    }

    public Optional<MemberEntity> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, email, password, point, created_at, updated_at FROM member WHERE email = ? and password = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, ROW_MAPPER, email, password));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Member getMemberById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        List<Member> members = jdbcTemplate.query(sql, new Object[]{id}, new MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public Member getMemberByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        List<Member> members = jdbcTemplate.query(sql, new Object[]{email}, new MemberRowMapper());
        return members.isEmpty() ? null : members.get(0);
    }

    public void addMember(Member member) {
        String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }

    public void updateMember(Member member) {
        String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Member> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }

    private static class MemberRowMapper implements RowMapper<Member> {

        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("point")
            );
        }
    }
}

