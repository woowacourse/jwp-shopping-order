package cart.persistence.dao;

import cart.persistence.entity.MemberEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class MemberDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private MemberDao memberDao;

    @Autowired
    public MemberDaoTest(JdbcTemplate jdbcTemplate) {
        this.memberDao = new MemberDao(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingColumns("email", "password")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    void 아이디로_멤버를_조회한다() {
        MemberEntity memberEntity = new MemberEntity(null, "c@c.com", "1234");
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(memberEntity);
        long memberId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();

        MemberEntity foundMember = memberDao.findById(memberId).get();

        assertThat(foundMember.getId()).isEqualTo(memberId);
    }

    @Test
    void 이메일로_멤버를_조회한다() {
        MemberEntity memberEntity = new MemberEntity(null, "c@c.com", "1234");
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(memberEntity);
        long memberId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();

        MemberEntity foundMember = memberDao.findByEmail("c@c.com").get();

        assertThat(foundMember.getId()).isEqualTo(memberId);
    }

    @Test
    void 멤버를_추가한다() {
        MemberEntity memberEntity = new MemberEntity(null, "c@c.com", "1234");
        long memberId = memberDao.add(memberEntity);

        String sql = "SELECT * FROM member WHERE id = ?";
        List<MemberEntity> memberEntities = jdbcTemplate.query(sql, new Object[]{memberId}, new MemberEntityRowMapper());

        assertThat(memberEntities.get(0).getId()).isEqualTo(memberId);
    }

    @Test
    void 멤버를_수정한다() {
        MemberEntity memberEntity = new MemberEntity(null, "c@c.com", "1234");
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(memberEntity);
        long memberId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();

        MemberEntity updatedMember = new MemberEntity(memberId, "update@c.com", "update");
        memberDao.update(updatedMember);

        String sql = "SELECT * FROM member WHERE id = ?";
        List<MemberEntity> memberEntities = jdbcTemplate.query(sql, new Object[]{memberId}, new MemberEntityRowMapper());

        assertThat(memberEntities.get(0).getEmail()).isEqualTo("update@c.com");
        assertThat(memberEntities.get(0).getPassword()).isEqualTo("update");
    }

    @Test
    void 멤버를_삭제한다() {
        MemberEntity memberEntity = new MemberEntity(null, "c@c.com", "1234");
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(memberEntity);
        long memberId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();

        memberDao.delete(memberId);

        String sql = "SELECT * FROM member WHERE id = ?";
        List<MemberEntity> memberEntities = jdbcTemplate.query(sql, new Object[]{memberId}, new MemberEntityRowMapper());
        assertThat(memberEntities).hasSize(0);
    }

    @Test
    void 모든_멤버를_조회한다() {
        MemberEntity memberEntity = new MemberEntity(null, "c@c.com", "1234");
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(memberEntity);
        long memberId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        MemberEntity memberEntity2 = new MemberEntity(null, "d@d.com", "1234");
        SqlParameterSource sqlParameterSource2 = new BeanPropertySqlParameterSource(memberEntity2);
        long memberId2 = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource2).longValue();

        List<MemberEntity> memberEntities = memberDao.findAll();
        assertThat(memberEntities.get(0).getId()).isEqualTo(memberId);
        assertThat(memberEntities.get(1).getId()).isEqualTo(memberId2);
    }

    private static class MemberEntityRowMapper implements RowMapper<MemberEntity> {
        @Override
        public MemberEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberEntity(rs.getLong("id"), rs.getString("email"), rs.getString("password"));
        }
    }
}
