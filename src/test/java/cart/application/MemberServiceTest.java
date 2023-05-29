package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberDao;
import cart.domain.member.Member;
import cart.repository.mapper.MemberMapper;
import cart.test.ServiceTest;
import cart.ui.controller.dto.response.MemberResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("getAllMembers 메서드는 모든 멤버를 응답한다.")
    void getAllMembers() {
        Member memberA = new Member("a@a.com", "password1", 0);
        Member memberB = new Member("b@b.com", "password2", 0);
        Long memberIdA = memberDao.addMember(MemberMapper.toEntity(memberA));
        Long memberIdB = memberDao.addMember(MemberMapper.toEntity(memberB));

        List<MemberResponse> response = memberService.getAllMembers();

        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(response.get(0).getId()).isEqualTo(memberIdA),
                () -> assertThat(response.get(0)).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(MemberResponse.from(memberA)),
                () -> assertThat(response.get(1).getId()).isEqualTo(memberIdB),
                () -> assertThat(response.get(1)).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(MemberResponse.from(memberB))
        );
    }

    @Test
    @DisplayName("getMemberByEmailAndPassword 메서드는 이메일, 비밀번호를 통해 멤버를 응답한다.")
    void GetMemberByEmailAndPassword() {
        Member member = new Member(1L, "a@a.com", "password1", 0);
        memberDao.addMember(MemberMapper.toEntity(member));

        MemberResponse response = memberService.getMemberByEmailAndPassword("a@a.com", "password1");

        assertAll(
                () -> assertThat(response.getId()).isEqualTo(member.getId()),
                () -> assertThat(response.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(response.getPassword()).isEqualTo(member.getPassword()),
                () -> assertThat(response.getPoint()).isEqualTo(member.getPoint())
        );
    }
}
