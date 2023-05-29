package cart.repository.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.entity.MemberEntity;
import cart.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberMapperTest {

    @Test
    @DisplayName("엔티티를 도메인으로 변환한다.")
    void convertFromEntityToDomain() {
        MemberEntity entity = new MemberEntity(1L, "a@a.com", "password1", 0);

        Member result = MemberMapper.toDomain(entity);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(entity.getId()),
                () -> assertThat(result.getEmail()).isEqualTo(entity.getEmail()),
                () -> assertThat(result.getPassword()).isEqualTo(entity.getPassword()),
                () -> assertThat(result.getPoint()).isEqualTo(entity.getPoint())
        );
    }

    @Test
    @DisplayName("도메인을 엔티티로 변환한다.")
    void convertFromDomainToEntity() {
        Member domain = new Member(1L, "a@a.com", "password1", 0);

        MemberEntity result = MemberMapper.toEntity(domain);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(domain.getId()),
                () -> assertThat(result.getEmail()).isEqualTo(domain.getEmail()),
                () -> assertThat(result.getPassword()).isEqualTo(domain.getPassword()),
                () -> assertThat(result.getPoint()).isEqualTo(domain.getPoint()),
                () -> assertThat(result.getCreatedAt()).isNull(),
                () -> assertThat(result.getUpdatedAt()).isNull()
        );
    }
}
