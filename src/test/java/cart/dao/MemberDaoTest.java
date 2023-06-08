package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"classpath:/schema.sql", "classpath:/init_member.sql"})
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        this.memberDao = new MemberDao(jdbcTemplate);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "a@a.com:12345:false",
        "a@a.com:1234:true",
        "a@b.com:1234:false"},
        delimiter = ':')
    @DisplayName("이메일, 비밀번호가 존재한다면 true, 아니면 false 가 반환된다.")
    void isEmailAndPasswordExist(String email, String password, boolean isExist) {
        // when
        boolean result = memberDao.isEmailAndPasswordExist(email, password);

        // then
        assertThat(result).isEqualTo(isExist);
    }
}