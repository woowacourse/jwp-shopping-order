package cart.config.auth;

import cart.dto.User;

public interface AuthProvider {
    User resolveUser(String authorization);
}
