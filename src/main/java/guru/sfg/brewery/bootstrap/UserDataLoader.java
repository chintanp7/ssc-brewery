package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDataLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private void loadUserAndAuthorities() {
        if (authorityRepository.count() == 0) {
            authorityRepository.save(Authority.builder().role("ADMIN").build());
            authorityRepository.save(Authority.builder().role("USER").build());
            authorityRepository.save(Authority.builder().role("CUSTOMER").build());

            log.debug("Authorities loaded: {}", authorityRepository.count());
        }

        if (userRepository.count() == 0) {
            userRepository.save(User.builder().authority(authorityRepository.findAuthorityByRole("ADMIN").get())
                    .username("spring").password(passwordEncoder.encode("guru")).build());
            userRepository.save(User.builder().authority(authorityRepository.findAuthorityByRole("USER").get())
                    .username("user").password(passwordEncoder.encode("password")).build());
            userRepository.save(User.builder().authority(authorityRepository.findAuthorityByRole("CUSTOMER").get())
                    .username("scott").password(passwordEncoder.encode("tiger")).build());

            log.debug("Users loaded: {}", userRepository.count());
        }
    }

    @Override
    public void run(String... args) throws Exception {
        loadUserAndAuthorities();
    }
}
