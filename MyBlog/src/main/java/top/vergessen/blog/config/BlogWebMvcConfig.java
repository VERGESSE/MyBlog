package top.vergessen.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.vergessen.blog.interceptor.ForeInterceptor;

import java.nio.charset.Charset;
import java.util.List;

/**
 * SpringMVC配置
 * @author Vergessen
 * @date 2020/7/5 11:22.
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BlogWebMvcConfig implements WebMvcConfigurer {

    /**
     * 实现后台管理系统转发
     * @param registry 视同控制器转发器
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("admin").setViewName("admin/index.html");
    }

    @Bean
    public HandlerInterceptor handlerInterceptor(){
        return new ForeInterceptor();
    }

    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor())
                .addPathPatterns("/","/index","/blog/**","/search/**","/article/v/**")
                .excludePathPatterns("/blog/css/**","/search/css/**");
    }
}
