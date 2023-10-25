package app.pedalaco.core.security;

import app.pedalaco.core.user.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider implements AuthenticationProvider {


    private final BCryptPasswordEncoder passwordEncoder;

    private final UserService userService;

    public AuthProvider(BCryptPasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        var account = userService.getByEmail(name).orElse(null);
        if (account == null)
            return null;

        if (passwordEncoder.matches(password, account.getHashedPassword())) {
            return new UsernamePasswordAuthenticationToken(account, password, account.getRoles()
                    .stream()
                    .map(role -> "ROLE_" + role.name().toUpperCase())
                    .map(SimpleGrantedAuthority::new).toList());
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
