package shop.persistence.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import shop.persistence.entity.MemberEntity;

import static org.assertj.core.api.Assertions.assertThat;

@Import(MemberDao.class)
class MemberDaoTest extends DaoTest {
    @Autowired
    private MemberDao memberDao;

    @DisplayName("회원을 추가할 수 있다.")
    @Test
    void insertMemberTest() {
        //given
        MemberEntity member = new MemberEntity("쥬니", "쥬니1234");

        //when
        Long savedId = memberDao.insertMember(member);

        //then
        MemberEntity findMember = memberDao.findById(savedId);
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
    }

    @DisplayName("이름으로 회원을 찾을 수 있다..")
    @Test
    void findMemberByNameTest() {
        //given
        MemberEntity member = new MemberEntity("쥬니", "쥬니1234");

        //when
        memberDao.insertMember(member);

        //then
        MemberEntity findMember = memberDao.findByName("쥬니");
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
    }

    @DisplayName("회원의 이름으로 존재 여부를 확인할 수 있다.")
    @Test
    void existMemberByNameTest() {
        //given
        MemberEntity member = new MemberEntity("쥬니", "쥬니1234");
        memberDao.insertMember(member);

        //when then
        Assertions.assertThat(memberDao.isExistByName("쥬니")).isEqualTo(Boolean.TRUE);
        Assertions.assertThat(memberDao.isExistByName("바다")).isEqualTo(Boolean.FALSE);
    }
}
