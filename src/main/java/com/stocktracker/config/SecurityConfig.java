package com.stocktracker.config;

import com.stocktracker.security.CustomAuthenticationFilter;
import com.stocktracker.security.CustomAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@AllArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    //private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login");
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(STATELESS);

        // Set unauthorized requests exception handler
        httpSecurity.exceptionHandling().authenticationEntryPoint(
                (request, response, ex) -> {
                    response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            ex.getMessage()
                    );}).and();

//        httpSecurity.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/api/user/**").permitAll()
//                .antMatchers("/api/auth/**").permitAll()
//                .antMatchers("/api/test/**").hasAnyAuthority("USER")
//                .anyRequest()
//                .authenticated();


        httpSecurity.authorizeRequests().antMatchers("/auth/login/**",
                "/auth/**", "/api/token/refresh/**", "/api/users/**").permitAll();
        httpSecurity.authorizeRequests().antMatchers(GET, "/api/user/**")
                .hasAnyAuthority("ROLE_USER");
        httpSecurity.authorizeRequests().antMatchers(POST, "/api/user/save/**")
                .hasAnyAuthority("ROLE_ADMIN");
        httpSecurity.authorizeRequests().anyRequest().authenticated();
        httpSecurity.addFilter(customAuthenticationFilter);
        httpSecurity.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class); // a filter for a specific class

        //httpSecurity.addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // a filter for a specific class
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }

    @Override @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
