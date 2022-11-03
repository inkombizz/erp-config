package com.inkombizz.security;

import com.inkombizz.entity.model.User;
import com.inkombizz.entity.model.UserDetails;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inkombizz.utils.RestUtil;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AppRequestFilter extends OncePerRequestFilter {

  @Autowired
  private RestUtil restUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
          throws ServletException {
    try {
      final String requestTokenHeader = request.getHeader("Authorization");

      if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

        User sysUser = restUtil.whoAmI(requestTokenHeader, "validate");
        // Once we get the token validate it.
        if (sysUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          UserDetails userDetails = new UserDetails(sysUser.getUsername(), "", new ArrayList<>());
          userDetails.setSetup(sysUser.getSetup());

          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          usernamePasswordAuthenticationToken.setDetails(userDetails);

          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
      }
      chain.doFilter(request, response);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
