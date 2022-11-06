package com.example.demo.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.example.demo.security.UserRole.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/","index")
                .permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
//                .antMatchers(HttpMethod.DELETE,"/managment/api/**").hasAuthority(AppUserPermission.COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.POST,"/managment/api/**").hasAuthority(AppUserPermission.COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT,"/managment/api/**").hasAuthority(AppUserPermission.COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.GET,"/managment/api/**").hasAnyRole(ADMIN.name(), ADMIN1.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses",true)
                .and()
                .rememberMe()
                .tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(21))
                .key("secretkeyy")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID","remember-me")
                .logoutSuccessUrl("/login");
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
       UserDetails user = User.builder()
                .username("otabek")
                .password(passwordEncoder.encode("1234"))
               // .roles(STUDENT.name())
               .authorities(STUDENT.getGranted())
                .build();
        UserDetails admin = User.builder()
                .username("Admin")
                .password(passwordEncoder.encode("admin123"))
                //.roles(ADMIN.name())
                .authorities(ADMIN.getGranted())
                .build();
        UserDetails admin1 = User.builder()
                .username("Admin1")
                .password(passwordEncoder.encode("admin1123"))
               // .roles(ADMIN1.name())
                .authorities(ADMIN1.getGranted())
                .build();
        return new InMemoryUserDetailsManager(
                user,admin,admin1
        );
    }
}
