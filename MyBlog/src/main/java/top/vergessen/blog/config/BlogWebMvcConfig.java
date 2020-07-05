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

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
    }

    @Bean
    public HandlerInterceptor handlerInterceptor(){
        return new ForeInterceptor();
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    /**
     * 配置拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor())
                .addPathPatterns("/","/index","/blog/**","/search/**","/blog-single/**")
                .excludePathPatterns("/blog/css/**","/search/css/**");
    }
}
