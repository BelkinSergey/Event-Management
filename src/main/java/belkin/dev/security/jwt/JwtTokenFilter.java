package belkin.dev.security.jwt;

import belkin.dev.user.dto.User;
import belkin.dev.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {


    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);
    private final UserService userService;
    private final JwtTokenManager jwtTokenManager;

    public JwtTokenFilter(@Lazy UserService userService,
                          JwtTokenManager jwtTokenManager) {
        this.userService = userService;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        var jwtToken = authorizationHeader.substring(7);

        String loginFromToken;
        try {
            loginFromToken = jwtTokenManager.getLoginFromToken(jwtToken);
        } catch (Exception e) {
            log.error("ошибка при чтении токена", e);
            filterChain.doFilter(request, response);
            return;
        }

        User user = userService.findByLogin(loginFromToken);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user,
                null,
                List.of(new SimpleGrantedAuthority(user.role().toString()))
        );
        SecurityContextHolder.getContext().setAuthentication(token);
        filterChain.doFilter(request, response);


    }

}
