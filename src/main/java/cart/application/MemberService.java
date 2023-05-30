package cart.application;

import cart.dao.MemberDao;
import cart.domain.Member;
import cart.dto.MemberRequest;
import cart.exception.AuthenticationException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void add(final MemberRequest memberRequest) {
        String encryptedPassword = encrypt(memberRequest.getPassword());
        memberDao.addMember(new Member(memberRequest.getName(), encryptedPassword));
    }

    public String generateBasicAuth(final MemberRequest memberRequest) {
        String encryptedPassword = encrypt(memberRequest.getPassword()); // encrypt util 클래스 생성
        if (!memberDao.existsByMember(new Member(memberRequest.getName(), encryptedPassword))) {
            throw new AuthenticationException();
        }
        String credentials = memberRequest.getName() + ":" + encryptedPassword;
        byte[] encodedBytes = Base64.encodeBase64(credentials.getBytes(StandardCharsets.UTF_8));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }

    private String encrypt(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // 예외 처리
        }

        return null;
    }
}
