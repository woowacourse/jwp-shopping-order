package com.woowahan.techcourse.member.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.woowahan.techcourse.member.domain.Member;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class MemberDaoTest {

    private MemberDao memberDao;

    @Autowired
    void setUp(JdbcTemplate jdbcTemplate) {
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Nested
    class 멤버_저장이_필요하지_않은_경우 {

        @Test
        void 저장이_가능하다() {
            // given
            Member member = new Member(null, "email", "password");

            // when
            long result = memberDao.addMember(member);

            // then
            assertSoftly(softly -> {
                softly.assertThat(result).isPositive();
                softly.assertThat(memberDao.getAllMembers()).hasSize(1);
            });
        }
    }

    @Nested
    class 멤버_저장이_필요한_경우 {

        @BeforeEach
        void setUp() {
            Member member = new Member(null, "email", "password");
            memberDao.addMember(member);
        }

        @Test
        void 없으면_빈_값을_반환한다() {
            // given
            Optional<Member> result = memberDao.findByEmailAndPassword("", "");

            // then
            assertThat(result).isEmpty();
        }

        @Test
        void 이메일_비밀번호가_일치하면_멤버를_반환한다() {
            // given
            Optional<Member> result = memberDao.findByEmailAndPassword("email", "password");

            // then
            assertSoftly(softly -> {
                        softly.assertThat(result).isPresent();
                        softly.assertThat(result.get().getEmail()).isEqualTo("email");
                        softly.assertThat(result.get().getPassword()).isEqualTo("password");
                    }
            );
        }
    }
}
