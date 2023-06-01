package com.woowahan.techcourse.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MemberTest {

    @Test
    void 멤버에_password_가_null_인_경우_검증() {
        // given
        Member member = new Member(1L, "email", null);

        // when
        boolean result = member.checkPassword(null);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 멤버에_password_가_null_이_아닌_경우_검증() {
        // given
        Member member = new Member(1L, "email", "password");

        // when
        boolean result = member.checkPassword("password");

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 멤버에_password_가_null_이_아닌_경우_검증_실패() {
        // given
        Member member = new Member(1L, "email", "password");

        // when
        boolean result = member.checkPassword("password1");

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 멤버에_password_가_null_인_경우_실패() {
        // given
        Member member = new Member(1L, "email", null);

        // when
        boolean result = member.checkPassword("password");

        // then
        assertThat(result).isFalse();
    }
}
