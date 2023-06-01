package cart.persistence.dao;

import cart.domain.member.Member;
import cart.persistence.entity.MemberEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MemberDao {

    private static final RowMapper<Member> MEMBER_MAPPER = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("point")
    );

    private static final RowMapper<MemberEntity> MEMBER_ENTITY_MAPPER = (rs, rowNum) -> new MemberEntity(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("point")
    );

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public MemberDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public MemberEntity getMemberById(final Long id) {
        final String sql = "SELECT id, email, password, point FROM member WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, MEMBER_ENTITY_MAPPER, id);
    }

    public Member getMemberByEmail(final String email) {
        final String sql = "SELECT id, email, password, point FROM member WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, MEMBER_MAPPER, email);
    }

    public void addMember(final Member member) {
        final SqlParameterSource source = new MapSqlParameterSource()
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());
        jdbcInsert.execute(source);
    }

    public void updateMember(final Member member) {
        final String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
    }

    public void deleteMember(final Long id) {
        final String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<MemberEntity> getAllMembers() {
        final String sql = "SELECT id, email, password, point from member";
        return jdbcTemplate.query(sql, MEMBER_ENTITY_MAPPER);
    }

    public void updatePoint(final MemberEntity memberEntity) {
        final String sql = "UPDATE member SET point = ? WHERE id = ?";
        jdbcTemplate.update(sql, memberEntity.getPoint(), memberEntity.getId());
    }

    public List<MemberEntity> getMembersByIds(final List<Long> memberIds) {
        if (memberIds.isEmpty()) {
            return new ArrayList<>();
        }

        final String sql = "SELECT id, email, password, point FROM member WHERE id IN (:id)";
        final MapSqlParameterSource source = new MapSqlParameterSource("id", memberIds);
        return namedJdbcTemplate.query(sql, source, MEMBER_ENTITY_MAPPER);
    }
}
