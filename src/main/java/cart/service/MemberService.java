package cart.service;

import static java.util.stream.Collectors.toUnmodifiableList;

import cart.dao.MemberDao;
import cart.dto.MemberDto;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional(readOnly = true)
    public List<MemberDto> findAll() {
        return memberDao.findAll().stream()
                .map(MemberDto::from)
                .collect(toUnmodifiableList());
    }
}
