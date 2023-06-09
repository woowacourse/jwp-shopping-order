package cart.persistence.member;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cart.domain.member.Member;

public class MemberRowMapper implements RowMapper<Member> {
	@Override
	public Member mapRow(final ResultSet rs, final int rowNum) throws SQLException {
		return new Member(
			rs.getLong("id"),
			rs.getString("email"),
			rs.getString("password")
		);
	}
}
