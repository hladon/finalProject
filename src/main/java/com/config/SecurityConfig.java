package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;


@EnableWebSecurity
@ComponentScan(basePackages = "com")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .authoritiesByUsernameQuery("SELECT u.phone,r.name FROM user u INNER JOIN USERS_ROLES ur ON " +
//                        "(u.id=ur.users) INNER JOIN ROLE r ON (ur.role=r.id) WHERE u.phone=?")
//                ;
        auth.inMemoryAuthentication().withUser("wert").password("wert").authorities("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/login","/").permitAll()
        .antMatchers("/user**").hasAuthority("USER")
        .and().formLogin().loginPage("/login").usernameParameter("phone").passwordParameter("password").permitAll().successHandler(authenticationSuccessHandler)
        .and().logout().permitAll()

        ;
    }
}
