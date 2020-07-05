package top.vergessen.blog.auth;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.vergessen.blog.exception.BlogException;
import top.vergessen.blog.exception.ExceptionEnum;
import top.vergessen.blog.util.JwtOperator;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 检验{@CheckLogin}的切面
 * @author Vergessen
 * @date 2020/7/5 11:22.
 */
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthAspect {
    private final JwtOperator jwtOperator;

    /**
     * 定义切面
     */
    @Pointcut("@annotation(top.vergessen.blog.auth.CheckLogin)")
    public void checkLogin(){ }

    @Before("checkLogin()")
    private void checkToken() {
        try {
            // 1. 从header里面获取token
            HttpServletRequest request = getHttpServletRequest();
            String token = request.getHeader("X-Token");

            // 2. 校验token是否合法&是否过期；如果不合法或已过期直接抛异常；如果合法放行
            if (!jwtOperator.validateToken(token)) {
                throw new BlogException(ExceptionEnum.TOKEN_INVALID);
            }
        } catch (Throwable throwable) {
            throw new BlogException(ExceptionEnum.TOKEN_INVALID);
        }
    }

    /**
     * 获取 HttpServletRequest
     * @return {@HttpServletRequest}
     */
    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        return Objects.requireNonNull(attributes).getRequest();
    }
}
