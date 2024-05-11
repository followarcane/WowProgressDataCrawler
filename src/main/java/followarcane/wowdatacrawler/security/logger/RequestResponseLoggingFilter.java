package followarcane.wowdatacrawler.security.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication != null ? authentication.getName() : "Anonymous";
        log.info("User: {}", currentUserName);

        log.info("Request  {} : {}", request.getMethod(), request.getRequestURI());
        filterChain.doFilter(request, response);
        log.info("Response {} : {}", response.getStatus(), response.getContentType());
    }
}
