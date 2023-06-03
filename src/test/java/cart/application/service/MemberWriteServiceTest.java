package cart.application.service;

import cart.application.repository.MemberRepository;
import cart.application.service.member.MemberWriteService;
import cart.ui.member.dto.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@Transactional
class MemberWriteServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberWriteService memberWriteService;

    @Test
    @DisplayName("사용자를 정상적으로 생성한다.")
    void createMember() {
        given(memberRepository.createMember(any()))
                .willReturn(1L);

        assertDoesNotThrow(() -> memberWriteService.createMember(new MemberRequest("레오", "leo@gmial.com", "leo123")));
    }

}
