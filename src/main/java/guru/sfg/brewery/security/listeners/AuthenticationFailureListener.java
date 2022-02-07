package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginFailureRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener {

    private final LoginFailureRepository loginFailureRepository;

    private final UserRepository userRepository;

    @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent event) {
        log.debug("Received Bad credentials.");

        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();

            if (token.getPrincipal() instanceof String) {
                String userName = token.getPrincipal().toString();
                log.debug("Username: " + token.getPrincipal());
                builder.userName(userName);

                Optional<User> user = userRepository.findByUsername(userName);
                if (user.isPresent()) {
                    builder.user(user.get());
                }
            }
            if (token.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
                log.debug("Source IP: " + details.getRemoteAddress());
                builder.sourceIp(details.getRemoteAddress());
            }

            LoginFailure loginFailure = loginFailureRepository.save(builder.build());
            log.debug("Login Failure saved. Id: " + loginFailure.getId());
        }
    }
}
