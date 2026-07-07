package com.myrestaurant.config;

import com.myrestaurant.service.ConfigService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// force la redirection vers /setup tant que rien n'est config
@Component
public class SetupInterceptor implements HandlerInterceptor {

  private final ConfigService configService;

  public SetupInterceptor(ConfigService configService) {
    this.configService = configService;
  }

  @Override
  public boolean preHandle(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull Object handler
  ) throws Exception {
    String path = request.getRequestURI();
    if (path.startsWith("/uploads") || path.startsWith("/error")) {
      return true;
    }
    boolean configured = configService.isConfigured();
    if (!configured && !path.startsWith("/setup")) {
      response.sendRedirect("/setup");
      return false;
    }
    if (configured && path.startsWith("/setup")) {
      response.sendRedirect("/");
      return false;
    }
    return true;
  }
}
