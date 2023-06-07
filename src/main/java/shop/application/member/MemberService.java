package shop.application.member;

import shop.application.member.dto.MemberDto;
import shop.application.member.dto.MemberJoinDto;
import shop.application.member.dto.MemberLoginDto;

import java.util.List;

public interface MemberService {

    void join(MemberJoinDto memberDto);

    String login(MemberLoginDto memberDto);

    List<MemberDto> getAllMembers();
}
