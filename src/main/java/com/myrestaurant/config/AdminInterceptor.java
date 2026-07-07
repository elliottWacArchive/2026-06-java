package com.myrestaurant.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// protege route admin par la session
@Component
public class AdminInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull Object handler
  ) throws Exception {
    String path = request.getRequestURI();
    if (path.startsWith("/admin/login")) {
      return true;
    }
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("admin") == null) {
      response.sendRedirect("/admin/login");
      return false;
    }
    return true;
  }
}
