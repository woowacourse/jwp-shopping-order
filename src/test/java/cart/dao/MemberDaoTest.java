package cart.dao;

import cart.entity.MemberEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.List;

import static cart.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class MemberDaoTest {

    private static final RowMapper<MemberEntity> ROW_MAPPER = (rs, rowNum) -> new MemberEntity(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("money"),
            rs.getInt("point")
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");

        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void getById() {
        MemberEntity 유저 = 다니.ENTITY;
        long memberId = saveMember(유저);

        MemberEntity saved = memberDao.getById(memberId).orElseGet(null);

        assertThat(saved)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(유저);
    }

    @Test
    void getByEmail() {
        MemberEntity 유저 = 다니.ENTITY;
        saveMember(유저);

        MemberEntity saved = memberDao.getByEmail(유저.getEmail()).orElseGet(null);

        assertThat(saved)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(유저);
    }

    @Test
    void insert() {
        MemberEntity 유저 = 주노.ENTITY;
        Long memberId = memberDao.insert(유저);

        String sql = "SELECT * FROM member WHERE id = ?";
        MemberEntity member = jdbcTemplate.queryForObject(sql, ROW_MAPPER, memberId);

        assertThat(member)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(유저);
    }

    @Test
    void update() {
        MemberEntity 유저 = 다니.ENTITY;
        long memberId = saveMember(유저);

        MemberEntity 유저_수정 = new MemberEntity(memberId, "유저_수정@email.com", "1q2w3e4r", 4000, 10);
        memberDao.update(유저_수정);

        String sql = "SELECT * FROM member WHERE id = ?";
        MemberEntity member = jdbcTemplate.queryForObject(sql, ROW_MAPPER, memberId);
        assertThat(member)
                .usingRecursiveComparison()
                .isEqualTo(유저_수정);
    }

    @Test
    void getAll() {
        saveMember(주노.ENTITY);
        saveMember(메리.ENTITY);
        saveMember(헤나.ENTITY);
        saveMember(다니.ENTITY);

        String sql = "SELECT * FROM member";
        List<MemberEntity> members = jdbcTemplate.query(sql, ROW_MAPPER);

        assertThat(members).hasSize(4);
    }

    private long saveMember(MemberEntity memberEntity) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(memberEntity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }
}
