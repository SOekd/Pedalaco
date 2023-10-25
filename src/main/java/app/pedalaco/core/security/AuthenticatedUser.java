package app.pedalaco.core.security;

import app.pedalaco.core.user.User;
import app.pedalaco.core.user.UserRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class AuthenticatedUser {

    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<User> get() {
        return authenticationContext.getAuthenticatedUser(User.class);
    }

    public void logout() {
        authenticationContext.logout();
    }

}
