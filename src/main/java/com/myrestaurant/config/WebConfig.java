package com.myrestaurant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final SetupInterceptor setupInterceptor;
  private final AdminInterceptor adminInterceptor;
  private final String uploadsDir;

  public WebConfig(
    SetupInterceptor setupInterceptor,
    AdminInterceptor adminInterceptor,
    @Value("${app.uploads-dir}") String uploadsDir
  ) {
    this.setupInterceptor = setupInterceptor;
    this.adminInterceptor = adminInterceptor;
    this.uploadsDir = uploadsDir;
  }

  @Override
  public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
    registry
      .addResourceHandler("/uploads/**")
      .addResourceLocations("file:" + uploadsDir + "/");
  }

  @Override
  public void addInterceptors(@NonNull InterceptorRegistry registry) {
    registry
      .addInterceptor(setupInterceptor)
      .excludePathPatterns("/uploads/**", "/error");
    registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/**");
  }
}
