package cart.persistence.member;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cart.domain.member.Member;
import cart.domain.member.MemberRepository;

@Repository
public class MemberJdbcRepository implements MemberRepository {

	private static final RowMapper<Member> MEMBER_ROW_MAPPER = new MemberRowMapper();

	private final JdbcTemplate jdbcTemplate;

	public MemberJdbcRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void addMember(Member member) {
		String sql = "INSERT INTO member (email, password) VALUES (?, ?)";
		jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
	}

	public List<Member> findAll() {
		String sql = "SELECT * from member";
		return jdbcTemplate.query(sql, MEMBER_ROW_MAPPER);
	}

	public Optional<Member> findById(Long id) {
		String sql = "SELECT * FROM member WHERE id = ?";

		try {
			final Member member = jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, id);
			return Optional.ofNullable(member);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public Optional<Member> findByEmail(String email) {
		String sql = "SELECT * FROM member WHERE email = ?";

		try {
			final Member member = jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, email);
			return Optional.ofNullable(member);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}

	}

	public void update(Member member) {
		String sql = "UPDATE member SET email = ?, password = ? WHERE id = ?";
		jdbcTemplate.update(sql, member.getEmail(), member.getPassword(), member.getId());
	}

	public void deleteById(Long id) {
		String sql = "DELETE FROM member WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}
}

