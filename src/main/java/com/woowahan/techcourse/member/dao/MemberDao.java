package com.woowahan.techcourse.member.dao;

import com.woowahan.techcourse.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private static final RowMapper<Member> ROW_MAPPER = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Optional<Member> findById(Long id) {
        String sql = "SELECT id, email, password FROM member WHERE id = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public long insert(Member member) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword());
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public List<Member> findAll() {
        String sql = "SELECT id, email, password FROM member";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, email, password FROM member WHERE email = ? AND password = ?";
        try {
            Member member = jdbcTemplate.queryForObject(sql, ROW_MAPPER, email, password);
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
