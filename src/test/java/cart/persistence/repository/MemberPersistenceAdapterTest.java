package cart.persistence.repository;

import cart.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Sql({"classpath:truncate.sql", "classpath:data.sql"})
class MemberPersistenceAdapterTest {

    private MemberPersistenceAdapter memberPersistenceAdapter;

    private Member member;

    @Autowired
    public MemberPersistenceAdapterTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.memberPersistenceAdapter = new MemberPersistenceAdapter(namedParameterJdbcTemplate);
        this.member = new Member(1L, "a@a.com", "1234", 0L);
    }

    @Test
    @DisplayName("email을 통해 찾을 수 있다")
    void findByEmail() {
        // given, when
        Optional<Member> found = memberPersistenceAdapter.findByEmail("a@a.com");
        // then
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("모든 멤버를 찾을 수 있다")
    void findAll() {
        // given, when
        List<Member> found = memberPersistenceAdapter.findAll();
        // then
        assertThat(found).isNotEmpty();
    }

    @Test
    @DisplayName("멤버를 업데이트 할 수 있다")
    void update() {
        // given, when
        memberPersistenceAdapter.update(
                new Member(member.getId(), member.getEmail(), "4321", member.getPoint()));
        Optional<Member> found = memberPersistenceAdapter.findByEmail("a@a.com");
        // then
        assertThat(found.get().getPassword()).isEqualTo("4321");
    }
}
