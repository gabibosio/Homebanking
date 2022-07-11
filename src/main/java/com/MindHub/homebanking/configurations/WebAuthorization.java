package com.MindHub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
class WebAuthorization extends WebSecurityConfigurerAdapter {
    @Override

    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/web/index.html","/web/Styles/login.css","/web/imagenes/**",
                        "/web/Script/index.js").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll()

                .antMatchers("/h2-console/**").hasAuthority("ADMIN")
                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/web/manager.html","/web/Styles/styles.css","/web/Script/manager.js").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/createLoans").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/api/cards/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/api/clients").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/api/clients/modificar").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/clients/create").hasAuthority("ADMIN")
                .antMatchers("/api/clients/current").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers(HttpMethod.POST,"/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/transactions").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/api/loans").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers(HttpMethod.PATCH,"/api/cards/expired/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH,"/api/accounts/visibility/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/pdf/generate").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/clients/cards/payments").permitAll()
                .antMatchers("/api/clients/**").hasAuthority("ADMIN")
                .antMatchers("/**").hasAuthority("CLIENT");



        http.formLogin()

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login");



        http.logout().logoutUrl("/api/logout");


        http.csrf().disable();

        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());


        //disabling frameOptions so h2-console can be accessed

        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }



    }


}
