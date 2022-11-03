package com.inkombizz.security;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AppAuthenticationEntryPoint appAuthenticationEntryPoint;

  @Autowired
  private UserDetailsService appUserDetailsService;

  @Autowired
  private AppRequestFilter appRequestFilter;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    // Disable CSRF (cross site request forgery)
    httpSecurity.csrf().disable();

    // Entry points
    httpSecurity.authorizeRequests()
            .antMatchers("/**/**/**/authenticate").permitAll()
            .antMatchers("/**/**").permitAll()
            .antMatchers("/**/**/**").permitAll()
            .antMatchers("/**/**/**/**").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**/**").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**/**/**").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**/**/**/**").permitAll()
            .anyRequest().authenticated();

    httpSecurity.exceptionHandling().authenticationEntryPoint(appAuthenticationEntryPoint).and().sessionManagement();
    httpSecurity.addFilterBefore(appRequestFilter, UsernamePasswordAuthenticationFilter.class);

    // No session will be created or used by spring security
    httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    
    httpSecurity.cors().configurationSource(request -> {
      CorsConfiguration corsCfg = new CorsConfiguration();

      // All origins, or specify the origins you need
//      corsCfg.setAllowCredentials(Boolean.TRUE);

      // All origins, or specify the origins you need
      corsCfg.addAllowedOriginPattern("*");

      // If you really want to allow all methods
//      corsCfg.addAllowedMethod(CorsConfiguration.ALL);
      // If you want to allow specific methods only
      corsCfg.addAllowedMethod(HttpMethod.GET);
      corsCfg.addAllowedMethod(HttpMethod.POST);
      corsCfg.addAllowedMethod(HttpMethod.PUT);
      corsCfg.addAllowedMethod(HttpMethod.DELETE);
      corsCfg.addAllowedMethod(HttpMethod.OPTIONS);

      corsCfg.setAllowedHeaders(Arrays.asList("Accept", "Access-Control-Request-Method", "Access-Control-Request-Headers",
              "Accept-Language", "Authorization", "Content-Type", "Request-Name", "Request-Surname", "Origin", "X-Request-AppVersion",
              "X-Request-OsVersion", "X-Request-Device", "X-Requested-With"));
      return corsCfg;
    });
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/v2/api-docs")
            .antMatchers("/**/swagger-resources/**")
            .antMatchers("/**/swagger-ui.html")
            .antMatchers("/**/configuration/**")
            .antMatchers("/**/webjars/**")
            .antMatchers("/**/public");
  }
}
