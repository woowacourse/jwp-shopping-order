package cart.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql(value = "classpath:test_truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;
    private MemberEntity memberEntity;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        memberEntity = new MemberEntity("huchu@woowahan.com", "1234567a!", 0);
    }

    @Test
    void id로_회원을_찾는다() {
        //given
        final Long id = memberDao.addMember(memberEntity);

        //when
        final MemberEntity memberEntity = memberDao.getMemberById(id);

        //then
        assertThat(memberEntity).usingRecursiveComparison()
                .isEqualTo(new MemberEntity(1L, "huchu@woowahan.com", "1234567a!", 0));
    }

    @Test
    void id로_회원을_찾을_때_입력한_id의_회원이_존재하지_않으면_예외를_던진다() {
        //given
        final Long id = Long.MIN_VALUE;

        //expect
        assertThatThrownBy(() -> memberDao.getMemberById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id : -9223372036854775808 인 회원이 존재하지 않습니다.");
    }

    @Test
    void 이메일로_회원을_찾는다() {
        //given
        memberDao.addMember(memberEntity);
        final String email = memberEntity.getEmail();

        //when
        final MemberEntity memberEntity = memberDao.getMemberByEmail(email);

        //then
        assertThat(memberEntity).usingRecursiveComparison()
                .isEqualTo(new MemberEntity(1L, "huchu@woowahan.com", "1234567a!", 0));
    }

    @Test
    void 이메일로_회원을_찾을_때_입력한_이메일의_회원이_존재하지_않으면_예외를_던진다() {
        //given
        final String email = "nothing@nothing.com";

        //expect
        assertThatThrownBy(() -> memberDao.getMemberByEmail(email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("email : nothing@nothing.com 인 회원이 존재하지 않습니다.");
    }

    @Test
    void 회원_정보를_저장한다() {
        //given
        final MemberEntity memberEntity = new MemberEntity("huchu@woowahan.com", "1234567a!", 0);

        //when
        final Long id = memberDao.addMember(memberEntity);

        //then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 회원_정보를_수정한다() {
        //given
        final Long id = memberDao.addMember(memberEntity);
        final MemberEntity pointUpdatedMember = new MemberEntity(id, memberEntity.getEmail(), memberEntity.getPassword(), 1000);

        //when
        final int affectedRows = memberDao.updateMember(pointUpdatedMember);

        //then
        assertThat(affectedRows).isEqualTo(1);
    }

    @Test
    void 회원_정보를_삭제한다() {
        //given
        final Long id = memberDao.addMember(memberEntity);

        //when
        final int affectedRows = memberDao.deleteMember(id);

        //then
        assertThat(affectedRows).isEqualTo(1);
        assertThatThrownBy(() -> memberDao.getMemberById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("id : " + id + " 인 회원이 존재하지 않습니다.");
    }

    @Test
    void 모든_회원_정보를_얻는다() {
        //given
        memberDao.addMember(memberEntity);

        //when
        final List<MemberEntity> allMembers = memberDao.getAllMembers();

        //then
        assertSoftly(softly -> {
            softly.assertThat(allMembers).hasSize(1);
            softly.assertThat(allMembers).usingRecursiveComparison()
                    .isEqualTo(List.of(new MemberEntity(1L, "huchu@woowahan.com", "1234567a!", 0)));
        });
    }
}
