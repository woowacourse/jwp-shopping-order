package com.woowahan.techcourse.member.application;

import com.woowahan.techcourse.member.dao.MemberDao;
import com.woowahan.techcourse.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberDao memberDao;

    public MemberQueryService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> getAllMembers() {
        return memberDao.findAll();
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password);
    }
}
