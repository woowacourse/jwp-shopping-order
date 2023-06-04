package cart.member.dao;

import cart.cartitem.repository.CartItemEntity;
import cart.member.domain.Member;
import cart.member.repository.MemberEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MemberDao {
    
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    private final RowMapper<MemberEntity> rowMapper = (rs, rowNum) ->
            new MemberEntity(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getLong("point")
            );
    
    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }
    
    public MemberEntity getMemberById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public MemberEntity getMemberByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, email);
    }

    public void addMember(MemberEntity memberEntity) {
        String sql = "INSERT INTO member (email, password, point) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getPoint());
    }

    public void updateMember(MemberEntity memberEntity) {
        String sql = "UPDATE member SET email = ?, password = ?, point = ? WHERE id = ?";
        jdbcTemplate.update(sql, memberEntity.getEmail(), memberEntity.getPassword(), memberEntity.getPoint(), memberEntity.getId());
    }

    public void deleteMember(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Member> getAllMembers() {
        String sql = "SELECT * from member";
        return jdbcTemplate.query(sql, new MemberRowMapper());
    }
    
    public void update(final MemberEntity memberEntity) {
        String sql = "UPDATE member SET email = ?, password = ?, point = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getPoint(),
                memberEntity.getId()
        );
    }
    
    private static class MemberRowMapper implements RowMapper<Member> {
        @Override
        public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Member(rs.getLong("id"), rs.getString("email"), rs.getString("password"), rs.getLong("point"));
        }
    }
}

