package cart.config;

import cart.dto.User;

public interface AuthProvider {
    User resolveUser(String authorization);
}
