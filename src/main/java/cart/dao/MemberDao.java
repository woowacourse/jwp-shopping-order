package cart.dao;

import cart.domain.Member;
import cart.entity.MemberEntity;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<MemberEntity> memberRowMapper = (rs, rowNum) ->
            new MemberEntity(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("grade"),
                    rs.getInt("total_purchase_amount")
            );

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public MemberEntity save(final MemberEntity memberEntity) {
        final SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(memberEntity);
        final long savedId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        return new MemberEntity(
                savedId,
                memberEntity.getEmail(),
                memberEntity.getPassword(),
                memberEntity.getGrade(),
                memberEntity.getTotalPurchaseAmount()
        );
    }

    public List<MemberEntity> findAll() {
        final String sql = "select * from member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }

    public MemberEntity findById(final Long id) {
        final String sql = "SELECT * FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, id);
    }

    public MemberEntity findByEmail(final String email) {
        final String sql = "select * from member where email = ?";
        return jdbcTemplate.queryForObject(sql, memberRowMapper, email);
    }

    public void update(final Member member) {
        final String sql = "update member set email = ?, password = ?, grade = ?, total_purchase_amount = ? where id = ?";
        jdbcTemplate.update(
                sql,
                member.getEmail(),
                member.getPassword(),
                member.getGrade().name(),
                member.getTotalPurchaseAmount(),
                member.getId()
        );
    }

    public void deleteById(final Long id) {
        final String sql = "delete from member where id = ?";
        jdbcTemplate.update(sql, memberRowMapper, id);
    }
}

