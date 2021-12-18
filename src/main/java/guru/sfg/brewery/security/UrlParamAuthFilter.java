package guru.sfg.brewery.security;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class UrlParamAuthFilter extends AbstractRestAuthFilter {

    public UrlParamAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }



    public String getUsername(HttpServletRequest request) {
        return request.getParameter("username");
    }

    public String getPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }
}
