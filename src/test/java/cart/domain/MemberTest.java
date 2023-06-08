package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MemberTest {

    @ParameterizedTest(name = "멤버의 비밀번호가 {0}일 때 {1}일 때 {2}를 반환한다.")
    @CsvSource(value = {"password:password:true", "password:different:false"}, delimiter = ':')
    void checkPassword_same(final String password, final String check, final boolean expectResult) {
        //given
        final Member member = new Member(null, "email", password);

        //when
        final boolean isSame = member.checkPassword(check);

        //then
        assertThat(isSame).isEqualTo(expectResult);
    }
}
