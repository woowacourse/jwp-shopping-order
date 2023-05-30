package cart.application;

import cart.application.dto.response.MemberResponse;
import cart.application.dto.response.PointResponse;
import cart.domain.member.Member;
import cart.persistence.dao.MemberDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional(readOnly = true)
    public List<MemberResponse> getAllMembers() {
        return memberDao.getAllMembers().stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PointResponse checkPoint(final Member member) {
        return new PointResponse(
                memberDao.getMemberById(member.getId()).getPoint()
        );
    }
}
