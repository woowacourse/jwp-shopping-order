package cart.ui;

import cart.domain.MemberGrade;
import org.springframework.core.convert.converter.Converter;

public class MemberGradeConverter implements Converter<String, MemberGrade> {
    @Override
    public MemberGrade convert(final String source) {
        try {
            return MemberGrade.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 회원 등급입니다.");
        }
    }
}
