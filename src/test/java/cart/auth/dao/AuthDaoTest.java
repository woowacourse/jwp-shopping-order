package cart.auth.dao;

import cart.exception.AuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthDaoTest {

    @Autowired
    private DataSource dataSource;

    private AuthDao authDao;

    @BeforeEach
    void setUp() {
        authDao = new AuthDao(dataSource);
    }

    @ParameterizedTest
    @CsvSource({"a@a.com, 1234",
            "b@b.com, 1234"})
    @Sql("/member_data.sql")
    @DisplayName("유저 조회 성공")
    void findIdByEmailAndPassword_success(final String email, final String password) {
        assertThatCode(() -> authDao.findIdByEmailAndPassword(email, password))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("잘못된 인증 정보이면 예외가 발생한다.")
    void findIdByEmailAndPassword_fail() {
        assertThatThrownBy(() -> authDao.findIdByEmailAndPassword("pobi", "pobipobi"))
                .isInstanceOf(AuthorizationException.class);
    }
}
