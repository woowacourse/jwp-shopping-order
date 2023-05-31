package cart.application.service;

import cart.application.repository.MemberRepository;
import cart.application.service.member.MemberReadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static cart.fixture.MemberFixture.디노;
import static cart.fixture.MemberFixture.비버;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberReadServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberReadService memberReadService;

    @Test
    @DisplayName("전체 사용자 목록을 조회한다.")
    void findAllMembersTest() {
        // given
        given(memberRepository.findAllMembers())
                .willReturn(List.of(디노, 비버));

        // when, then
        assertDoesNotThrow(() -> memberReadService.findAllMembers());
    }

    @Test
    @DisplayName("아이디로 특정 사용자 정보를 조회한다.")
    void findMemberByIdTest() {
        // given
        given(memberRepository.findMemberById(any()))
                .willReturn(Optional.of(비버));

        // when, then
        assertThat(memberReadService.findMemberById(any()))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(비버);
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 사용자 조회 시 예외 처리한다.")
    void findMemberByIdExceptionTest() {
        // given
        given(memberRepository.findMemberById(any()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberReadService.findMemberById(any()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 사용자가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("이메일로 특정 사용자 정보를 조회한다.")
    void findMemberByEmailTest() {
        // given
        given(memberRepository.findMemberByEmail(any()))
                .willReturn(Optional.of(비버));

        // when, then
        assertThat(memberReadService.findMemberByEmail(any()))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(비버);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 사용자 조회 시 예외 처리한다.")
    void findMemberByEmailExceptionTest() {
        // given
        given(memberRepository.findMemberByEmail(any()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberReadService.findMemberByEmail(any()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 사용자가 존재하지 않습니다.");
    }

}
