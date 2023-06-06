package cart.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.response.MemberResponse;
import cart.exception.BadRequestException;

class MemberServiceTest {

    @Mock
    private MemberDao memberDao;

    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        memberService = new MemberService(memberDao);
    }

    @Test
    @DisplayName("로그인 성공 시 아무 동작을 하지 않는다.")
    public void testLogInSuccess() {
        String authorization = "Basic dGVzdEBleGFtcGxlLmNvbTpwYXNzd29yZA==";

        Member member = new Member(1L, "test@example.com", "password", "Test User");

        when(memberDao.findByEmail("test@example.com")).thenReturn(member);

        memberService.logIn(authorization);

        verify(memberDao, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("로그인 실패 시 BadRequestException을 던진다.")
    public void testLogInFailure() {
        String authorization = "Basic dGVzdEBleGFtcGxlLmNvbTpwYXNzd29yZA==";

        Member member = new Member(1L, "test@example.com", "password", "Test User");
        Member member2 = new Member(2L, "test1@example.com", "password2", "Test User");

        when(memberDao.findByEmail(member.getEmail())).thenReturn(member2);

        assertThrows(BadRequestException.class, () -> memberService.logIn(authorization));

        verify(memberDao, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("회원 프로필을 조회한다.")
    public void testFindProfile() {
        Member member = new Member(1L, "test@example.com", "password", "Test User");

        MemberResponse expectedResponse = new MemberResponse(member);

        MemberResponse actualResponse = memberService.findProfile(member);

        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getEmail(), actualResponse.getEmail());
        assertEquals(expectedResponse.getNickname(), actualResponse.getNickname());

        verify(memberDao, never()).findByEmail(anyString());
    }
}
