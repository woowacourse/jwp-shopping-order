package cart.dao;

import cart.entity.MemberEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MemberEntity findById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        final List<MemberEntity> memberEntities = jdbcTemplate.query(sql, new Object[]{id}, new MemberEntityRowMapper());
        return memberEntities.isEmpty() ? null : memberEntities.get(0);
    }

    public MemberEntity getMemberByEmail(final String email) {
        final String sql = "SELECT * FROM member WHERE email = ?";
        final List<MemberEntity> memberEntities = jdbcTemplate.query(sql, new Object[]{email}, new MemberEntityRowMapper());
        return memberEntities.isEmpty() ? null : memberEntities.get(0);
    }

    public void addMember(final MemberEntity memberEntity) {
        final String sql = "INSERT INTO member (email, password, grade) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getGrade());
    }

    public void updateMember(final MemberEntity memberEntity) {
        final String sql = "UPDATE member SET email = ?, password = ?, grade = ? WHERE id = ?";
        jdbcTemplate.update(sql, memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getId(), memberEntity.getGrade());
    }

    public void deleteMember(final Long id) {
        final String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<MemberEntity> findAll() {
        final String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberEntityRowMapper());
    }

    private static class MemberEntityRowMapper implements RowMapper<MemberEntity> {
        @Override
        public MemberEntity mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new MemberEntity(rs.getLong("id"), rs.getString("grade"), rs.getString("email"), rs.getString("password"));
        }
    }
}

