package com.woowahan.techcourse.member.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.woowahan.techcourse.member.domain.Member;
import java.util.List;
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
            long result = memberDao.insert(member);

            // then
            assertSoftly(softly -> {
                softly.assertThat(result).isPositive();
                softly.assertThat(memberDao.findAll()).hasSize(1);
            });
        }

        @Test
        void 없는_멤버를_조회하면_빈_값을_반환한다() {
            // given
            Optional<Member> result = memberDao.findById(1L);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    class 멤버_저장이_필요한_경우 {

        private long memberId;

        @BeforeEach
        void setUp() {
            Member member = new Member(null, "email", "password");
            memberId = memberDao.insert(member);
        }

        @Test
        void 있으면_조회가_가능하다() {
            // given
            Optional<Member> result = memberDao.findById(memberId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(result).isPresent();
                softly.assertThat(result.get().getEmail()).isEqualTo("email");
                softly.assertThat(result.get().getPassword()).isEqualTo("password");
            });
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

        @Test
        void 전체_조회가_가능하다() {
            // given
            Member member = new Member(null, "email2", "password2");
            memberDao.insert(member);

            // when
            List<Member> result = memberDao.findAll();

            assertSoftly(softly -> {
                softly.assertThat(result).hasSize(2);
                softly.assertThat(result.get(0).getEmail()).isEqualTo("email");
                softly.assertThat(result.get(0).getPassword()).isEqualTo("password");
                softly.assertThat(result.get(1).getEmail()).isEqualTo("email2");
                softly.assertThat(result.get(1).getPassword()).isEqualTo("password2");
            });
        }
    }
}
