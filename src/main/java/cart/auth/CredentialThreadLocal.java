package cart.auth;

import org.springframework.stereotype.Component;

@Component
public class CredentialThreadLocal {

    private final ThreadLocal<Credential> local = new ThreadLocal<>();

    public void set(final Credential credential) {
        local.set(credential);
    }

    public Credential getAndClear() {
        final Credential credential = local.get();
        local.remove();
        return credential;
    }
}
