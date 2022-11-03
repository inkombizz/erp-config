package com.inkombizz.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.inkombizz.AuthenticationConstant;
import com.inkombizz.ant.Authenticate;
import com.inkombizz.common.enums.EnumTrx;
import com.inkombizz.entity.model.RoleAuthorization;
import com.inkombizz.entity.model.User;
import com.inkombizz.repo.SysRoleAuthorizationRepository;
import com.inkombizz.repo.SysUserRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AuthenticationInterceptor implements HandlerInterceptor {

  @Autowired
  private SysUserRepository userRepo;

  @Autowired
  private SysRoleAuthorizationRepository roleAuthRepo;

  @Transactional
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HandlerMethod hm = (HandlerMethod) handler;
    final String requestTokenHeader = request.getHeader("Authorization");
    Authenticate authAnnotation = AnnotationUtils.findAnnotation(hm.getMethod(), Authenticate.class);
    if (authAnnotation == null) {
      return true;
    }

    if (StringUtils.isNotBlank(requestTokenHeader)) {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      String username = "";
      if (principal != null && principal instanceof UserDetails) {
        username = ((UserDetails) principal).getUsername();
      }

      if (StringUtils.isBlank(username)) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, AuthenticationConstant.UNAUTHORIZED_MESSAGE);
        return false;
      }

      String[] methodServices = authAnnotation.value();
      if (methodServices == null || methodServices[0] == null || methodServices[0].equals("")
              || methodServices[1] == null || methodServices[1].equals("")) {
        return true;
      }

      User user = userRepo.findByUsername(username);
      if (user.isSuperUser()) {
        return true;
      }

      RoleAuthorization ra = roleAuthRepo.findByHeaderCodeAndAuthorizationCode(user.getRole().getCode(), methodServices[0]);
      if (ra != null) {
        if (methodServices[1].equals(EnumTrx.VIEW.name())) {
          if (ra.isAssignAuthority()) {
            return true;
          }
        } else if (methodServices[1].equals(EnumTrx.SAVE.name())) {
          if (ra.isSaveAuthority()) {
            return true;
          }
        } else if (methodServices[1].equals(EnumTrx.UPDATE.name())) {
          if (ra.isUpdateAuthority()) {
            return true;
          }
        } else if (methodServices[1].equals(EnumTrx.DELETE.name())) {
          if (ra.isDeleteAuthority()) {
            return true;
          }
        } else if (methodServices[1].equals(EnumTrx.PRINT.name())) {
          if (ra.isPrintAuthority()) {
            return true;
          }
        }
      }
    }
    response.sendError(HttpServletResponse.SC_FORBIDDEN, AuthenticationConstant.FORBIDDEN_MESSAGE);
    return false;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
  }
}
