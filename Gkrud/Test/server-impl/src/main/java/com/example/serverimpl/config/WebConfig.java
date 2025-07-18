package com.example.serverimpl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor())
                // 게시글 작성/수정/삭제 권한이 필요한 경로
                .addPathPatterns("/api/posts/**")
                // 목록 조회(GET /api/posts)와 상세 조회(GET /api/posts/{id})는 제외
                .excludePathPatterns("/api/posts", "/api/posts/*");
    }
}
