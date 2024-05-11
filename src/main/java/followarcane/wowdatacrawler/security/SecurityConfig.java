package followarcane.wowdatacrawler.security;

import followarcane.wowdatacrawler.security.logger.RequestResponseLoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RequestResponseLoggingFilter requestResponseLoggingFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(requestResponseLoggingFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api/v1/wdc/**").hasRole("FAKE_ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}