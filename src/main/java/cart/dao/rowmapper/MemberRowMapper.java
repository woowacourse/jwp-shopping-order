package cart.dao.rowmapper;

import cart.entity.MemberEntity;
import org.springframework.jdbc.core.RowMapper;

public final class MemberRowMapper {

    private MemberRowMapper() {
    }

    public static final RowMapper<MemberEntity> memberEntity = (rs, rowNum) -> {
        return new MemberEntity(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getBigDecimal("money"),
                rs.getBigDecimal("point")
        );
    };
}
