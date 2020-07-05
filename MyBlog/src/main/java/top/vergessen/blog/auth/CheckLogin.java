package top.vergessen.blog.auth;

import java.lang.annotation.*;

/**
 * 为方法打上此注解则需要进行登录验证
 * @author Vergessen
 * @date 2020/7/5 11:40.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckLogin {
}
