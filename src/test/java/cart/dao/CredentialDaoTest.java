package cart.dao;

import cart.auth.Credential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class CredentialDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert memberJdbcInsert;

    private CredentialDao credentialDao;

    @BeforeEach
    void setUp() {
        credentialDao = new CredentialDao(jdbcTemplate);
        memberJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingColumns("email", "password")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    void 이메일을_입력받아_동일한_이메일을_가진_사용자를_조회한다() {
        // given
        final MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("email", "pizza1@pizza.com");
        parameter.addValue("password", "password");
        memberJdbcInsert.executeAndReturnKey(parameter).longValue();

        // when
        final Credential credential = credentialDao.findByEmail("pizza1@pizza.com").get();

        // then
        assertAll(
                () -> assertThat(credential.getEmail()).isEqualTo("pizza1@pizza.com"),
                () -> assertThat(credential.getPassword()).isEqualTo("password")
        );
    }

    @Test
    void 동일한_이메일을_가진_사용자가_없다면_Optional_Empty를_반환한다() {
        // expect
        assertThat(credentialDao.findByEmail("pizza1@pizza.com")).isNotPresent();
    }
}
