package top.vergessen.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;
import top.vergessen.blog.interceptor.CrawlerInterceptor;
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

    /**
     * 配置静态访问资源
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/admin/static/**").addResourceLocations("classpath:/static/admin/admin/static/");
    }


    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 反爬拦截器
        registry.addInterceptor(crawlerInterceptor())
                .addPathPatterns("/","/index","/blog/**","/search/**","/article/v/**")
                .excludePathPatterns("/blog/css/**","/search/css/**");
        // 日志拦截器
        registry.addInterceptor(foreInterceptor())
                .addPathPatterns("/","/index","/blog/**","/search/**","/article/v/**")
                .excludePathPatterns("/blog/css/**","/search/css/**");
    }

    @Bean
    public HandlerInterceptor foreInterceptor(){
        return new ForeInterceptor();
    }

    @Bean
    public HandlerInterceptor crawlerInterceptor(){
        return new CrawlerInterceptor();
    }
}
