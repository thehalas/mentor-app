package com.obss.jsi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // doto add csrf functionality in the future
                .authorizeRequests()
                .antMatchers("/", "/login","/assets/**" ,"/js/**").permitAll()
                .anyRequest().fullyAuthenticated()

                .and().formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard",true)
                .and().rememberMe()
                .and().logout()

                .and().oauth2Login()
                .loginPage("/login").defaultSuccessUrl("/dashboard", true)
                .and().logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID" );
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:8389/dc=springframework,dc=org")
                .and()
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPassword");
    }
}