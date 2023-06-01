package cart.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.MemberResponse;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<MemberResponse> getAllMembers() {
        final List<Member> members = memberDao.getAllMembers();
        return members.stream()
                .map(MemberResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }
}
