package cart.persistence.member;

import cart.application.repository.member.MemberRepository;
import cart.domain.member.Member;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJdbcRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Member> memberRowMapper = (rs, rowNum) ->
            new Member(rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
            );

    private final RowMapper<Boolean> booleanRowMapper = (rs, rowNum) ->
            rs.getBoolean("isExist");

    public MemberJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long createMember(final Member member) {
        final SqlParameterSource parameter = new BeanPropertySqlParameterSource(member);
        return simpleJdbcInsert.executeAndReturnKey(parameter).longValue();
    }

    @Override
    public List<Member> findAllMembers() {
        final String sql = "SELECT id, name, email, password FROM member";
        return jdbcTemplate.query(sql, memberRowMapper);
    }

    @Override
    public Optional<Member> findMemberById(final Long id) {
        final String sql = "SELECT id, name, email, password FROM member WHERE id = ?";
        try {
            final Member member = jdbcTemplate.queryForObject(sql, memberRowMapper, id);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findMemberByEmail(final String email) {
        final String sql = "SELECT id, name, email, password FROM member WHERE email = ?";
        try {
            final Member member = jdbcTemplate.queryForObject(sql, memberRowMapper, email);
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}

