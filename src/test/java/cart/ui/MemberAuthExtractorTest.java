package cart.ui;

import cart.domain.Member;
import cart.exception.AuthenticationException;
import common.DatabaseSetting;
import common.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static common.DatabaseSetting.Fixture.멤버_A;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@Import({DatabaseSetting.class, MemberAuthExtractor.class})
class MemberAuthExtractorTest {

    @Autowired
    DatabaseSetting databaseSetting;

    @Autowired
    MemberAuthExtractor memberAuthExtractor;

    @BeforeEach
    void setUp() {
        databaseSetting.setUp();
    }

    @AfterEach
    void clearUp() {
        databaseSetting.clearDatabase();
    }

    @Test
    void 인증에_성공하면_Member_객체를_반환한다() {
        // given
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        final String basicAuthHeaderValue = TestUtils.toBasicAuthHeaderValue(멤버_A.getEmail() + ":" + 멤버_A.getPassword());
        mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, basicAuthHeaderValue);

        // when
        final Member member = memberAuthExtractor.extract(mockHttpServletRequest);

        // then
        assertThat(member.getEmail()).isEqualTo(멤버_A.getEmail());
    }

    @Test
    void basic인증이_아니면_예외가_발생한다() {
        // given
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

        // when && then
        assertThatThrownBy(() -> memberAuthExtractor.extract(mockHttpServletRequest))
                .isInstanceOf(AuthenticationException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"z@z.com:1234", "a@a.com:5678"})
    void 이메일이_없거나_패스워드가_일치하지_않으면_예외를_발생시킨다(String memberInfo) {
        // given
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        final String basicAuthHeaderValue = TestUtils.toBasicAuthHeaderValue(memberInfo);
        mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, basicAuthHeaderValue);

        // when && then
        assertThatThrownBy(() -> memberAuthExtractor.extract(mockHttpServletRequest))
                .isInstanceOf(AuthenticationException.class);
    }
}
