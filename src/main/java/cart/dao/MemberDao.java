package cart.dao;

import cart.dao.rowmapper.MemberRowMapper;
import cart.domain.vo.Money;
import cart.entity.MemberEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cart.dao.support.SqlHelper.sqlHelper;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Long insertMember(MemberEntity memberEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                        .addValue("email", memberEntity.getEmail())
                        .addValue("password", memberEntity.getPassword())
                        .addValue("money", memberEntity.getMoney())
                        .addValue("point", memberEntity.getPoint()))
                .longValue();
    }

    public Optional<MemberEntity> getByMemberId(Long memberId) {
        String sql = sqlHelper()
                .select().condition("*")
                .from().table("member")
                .where().condition("id = ?")
                .toString();

        try {
            MemberEntity memberEntity = jdbcTemplate.queryForObject(sql, MemberRowMapper.memberEntity, memberId);
            return Optional.ofNullable(memberEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<MemberEntity> getByMemberEmail(String email) {
        String sql = sqlHelper()
                .select().columns("*")
                .from().table("member")
                .where().condition("email = ?")
                .toString();

        try {
            MemberEntity memberEntity = jdbcTemplate.queryForObject(sql, MemberRowMapper.memberEntity, email);
            return Optional.ofNullable(memberEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<MemberEntity> getAllMembers() {
        String sql = sqlHelper()
                .select().columns("*")
                .from().table("member")
                .toString();

        return jdbcTemplate.query(sql, MemberRowMapper.memberEntity);
    }

    public void updateMember(Long memberId, MemberEntity memberEntity) {
        String sql = sqlHelper()
                .update().table("member")
                .set("email = ?, password = ?")
                .where().condition("id = ?")
                .toString();

        jdbcTemplate.update(sql,
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberId);
    }

    public void updateMinusMoney(Long memberId, Money minusMoney) {
        String sql = sqlHelper()
                .update().set("money = money - ?")
                .from().table("member")
                .where().condition("id = ?")
                .toString();

        jdbcTemplate.update(sql, minusMoney.getValue(), memberId);
    }

    public void deleteMember(Long id) {
        String sql = sqlHelper()
                .delete()
                .from().table("member")
                .where().condition("id = ?")
                .toString();
        
        jdbcTemplate.update(sql, id);
    }
}
